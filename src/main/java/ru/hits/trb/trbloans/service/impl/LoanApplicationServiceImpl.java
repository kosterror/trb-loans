package ru.hits.trb.trbloans.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.trb.trbloans.client.core.CoreClient;
import ru.hits.trb.trbloans.client.core.dto.AccountDto;
import ru.hits.trb.trbloans.client.users.UsersClient;
import ru.hits.trb.trbloans.client.users.dto.ClientDto;
import ru.hits.trb.trbloans.dto.UnidirectionalTransactionDto;
import ru.hits.trb.trbloans.dto.loan.LoanDto;
import ru.hits.trb.trbloans.dto.loanapplication.LoanApplicationDto;
import ru.hits.trb.trbloans.dto.loanapplication.NewLoanApplicationDto;
import ru.hits.trb.trbloans.entity.LoanApplicationEntity;
import ru.hits.trb.trbloans.entity.LoanEntity;
import ru.hits.trb.trbloans.entity.LoanRepaymentEntity;
import ru.hits.trb.trbloans.entity.enumeration.LoanApplicationState;
import ru.hits.trb.trbloans.entity.enumeration.LoanRepaymentState;
import ru.hits.trb.trbloans.entity.enumeration.LoanState;
import ru.hits.trb.trbloans.exception.ConflictException;
import ru.hits.trb.trbloans.exception.NotFoundException;
import ru.hits.trb.trbloans.mapper.LoanApplicationMapper;
import ru.hits.trb.trbloans.mapper.LoanMapper;
import ru.hits.trb.trbloans.producer.LoanPaymentProducer;
import ru.hits.trb.trbloans.repository.LoanApplicationRepository;
import ru.hits.trb.trbloans.repository.LoanRepaymentRepository;
import ru.hits.trb.trbloans.repository.LoanRepository;
import ru.hits.trb.trbloans.service.LoanApplicationService;
import ru.hits.trb.trbloans.service.TariffService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanRepository loanRepository;
    private final LoanRepaymentRepository loanRepaymentRepository;
    private final LoanApplicationMapper loanApplicationMapper;
    private final LoanMapper loanMapper;
    private final TariffService tariffService;
    private final UsersClient usersClient;
    private final CoreClient coreClient;
    private final LoanPaymentProducer loanPaymentProducer;

    @Override
    @Transactional
    public LoanApplicationDto createLoanApplication(NewLoanApplicationDto dto) {
        var tariff = tariffService.findTariff(dto.getTariffId());
        var loanApplication = loanApplicationMapper.newDtoToEntity(dto);
        loanApplication.setTariff(tariff);

        return loanApplicationMapper.mapEntityToDto(loanApplicationRepository.save(loanApplication));
    }

    @Override
    @Transactional
    public LoanDto approveLoanApplication(UUID loanApplicationId, UUID officerId) {
        var loanApplication = findLoanApplicationForAction(loanApplicationId);

        loanApplication.setState(LoanApplicationState.APPROVED);
        loanApplication.setUpdatedDateFinal(new Date());
        loanApplication.setOfficerId(officerId);

        var clientDto = usersClient.getClient(loanApplication.getClientId());
        var accountDto = coreClient.createLoanAccount(clientDto.getId(),
                clientDto.getFirstName(),
                clientDto.getLastName(),
                clientDto.getPatronymic()
        );

        var loan = buildLoanEntity(loanApplication, clientDto, accountDto);
        var repayments = buildRepayments(loan);
        loan.setRepayments(repayments);
        loan = loanRepository.save(loan);

        loanRepaymentRepository.saveAll(repayments);

        var paymentTransaction = UnidirectionalTransactionDto.builder()
                .accountId(loan.getAccountId())
                .amount(loan.getIssuedAmount())
                .build();

        loanPaymentProducer.sendMessage(loan.getId(), paymentTransaction);

        return loanMapper.entityToDto(loan);
    }

    @Override
    @Transactional
    public LoanApplicationDto rejectLoanApplication(UUID loanApplicationId, UUID officerId) {
        var loanApplication = findLoanApplicationForAction(loanApplicationId);
        loanApplication.setState(LoanApplicationState.REJECTED);
        loanApplication.setOfficerId(officerId);
        loanApplication.setUpdatedDateFinal(new Date());

        return loanApplicationMapper.mapEntityToDto(loanApplicationRepository.save(loanApplication));
    }

    @Override
    @Transactional
    public List<LoanApplicationDto> getClientLoanApplications(UUID clientId,
                                                              LoanApplicationState loanApplicationState
    ) {
        return loanApplicationRepository.getAllByClientIdAndState(clientId, loanApplicationState)
                .stream()
                .map(loanApplicationMapper::mapEntityToDto)
                .toList();
    }

    @Override
    @Transactional
    public List<LoanApplicationDto> getLoanApplications(LoanApplicationState loanApplicationState) {
        return loanApplicationRepository.getAllByState(loanApplicationState)
                .stream()
                .map(loanApplicationMapper::mapEntityToDto)
                .toList();
    }

    private LoanApplicationEntity findLoanApplicationForAction(UUID id) {
        var loanApplication = findLoanApplication(id);

        if (loanApplication.getState() != LoanApplicationState.UNDER_CONSIDERATION) {
            throw new ConflictException("Invalid loan application state for action, it must be UNDER_CONSIDERATION'");
        }

        return loanApplication;
    }

    private LoanApplicationEntity findLoanApplication(UUID id) {
        return loanApplicationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("LoanApplication with id '" + id + "' not found"));
    }

    private LoanEntity buildLoanEntity(LoanApplicationEntity loanApplication,
                                       ClientDto clientDto,
                                       AccountDto accountDto
    ) {
        var interestRate = loanApplication.getTariff()
                .getInterestRate()
                .divide(BigDecimal.valueOf(100), RoundingMode.DOWN);
        var issuedAmount = loanApplication.getIssuedAmount();
        var amountLoan = interestRate
                .multiply(BigDecimal.valueOf(issuedAmount))
                .multiply(BigDecimal.valueOf(loanApplication.getLoanTermInDays()))
                .add(BigDecimal.valueOf(issuedAmount));

        var issuedDate = new Date();
        var calendar = Calendar.getInstance();
        calendar.setTime(issuedDate);
        calendar.add(Calendar.DAY_OF_MONTH, loanApplication.getLoanTermInDays());
        var repaymentDate = calendar.getTime();

        return LoanEntity.builder()
                .issuedDate(issuedDate)
                .repaymentDate(repaymentDate)
                .issuedAmount(loanApplication.getIssuedAmount())
                .amountLoan(amountLoan.longValue())
                .amountDebt(amountLoan.longValue())
                .accruedPenny(0)
                .loanTermInDays(loanApplication.getLoanTermInDays())
                .clientId(clientDto.getId())
                .accountId(accountDto.getId())
                .state(LoanState.OPEN)
                .tariff(loanApplication.getTariff())
                .loanApplication(loanApplication)
                .build();
    }

    private List<LoanRepaymentEntity> buildRepayments(LoanEntity loan) {
        var loanRepayments = new ArrayList<LoanRepaymentEntity>();
        var repaymentAmount = loan.getAmountLoan() / loan.getLoanTermInDays();

        var calendar = Calendar.getInstance();
        calendar.setTime(loan.getIssuedDate());

        for (int i = 0; i < loan.getLoanTermInDays(); i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            var repaymentDate = calendar.getTime();
            var loanRepayment = LoanRepaymentEntity.builder()
                    .date(repaymentDate)
                    .amount(repaymentAmount)
                    .state(LoanRepaymentState.OPEN)
                    .loan(loan)
                    .build();

            loanRepayments.add(loanRepayment);
        }

        return loanRepayments;
    }

}
