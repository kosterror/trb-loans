package ru.hits.trb.trbloans.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.trb.trbloans.client.core.CoreClient;
import ru.hits.trb.trbloans.client.core.dto.AccountDto;
import ru.hits.trb.trbloans.client.users.UsersClient;
import ru.hits.trb.trbloans.client.users.dto.ClientDto;
import ru.hits.trb.trbloans.dto.loanapplication.LoanApplicationDecisionDto;
import ru.hits.trb.trbloans.dto.loanapplication.LoanApplicationDto;
import ru.hits.trb.trbloans.dto.loanapplication.NewLoanApplicationDto;
import ru.hits.trb.trbloans.dto.transaction.InitTransactionDto;
import ru.hits.trb.trbloans.dto.transaction.TransactionType;
import ru.hits.trb.trbloans.entity.LoanApplicationEntity;
import ru.hits.trb.trbloans.entity.LoanEntity;
import ru.hits.trb.trbloans.entity.enumeration.LoanApplicationState;
import ru.hits.trb.trbloans.entity.enumeration.LoanState;
import ru.hits.trb.trbloans.exception.ConflictException;
import ru.hits.trb.trbloans.exception.NotFoundException;
import ru.hits.trb.trbloans.mapper.LoanApplicationMapper;
import ru.hits.trb.trbloans.producer.TransactionProducer;
import ru.hits.trb.trbloans.repository.LoanApplicationRepository;
import ru.hits.trb.trbloans.repository.LoanRepository;
import ru.hits.trb.trbloans.service.LoanApplicationService;
import ru.hits.trb.trbloans.service.TariffService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanRepository loanRepository;
    private final LoanApplicationMapper loanApplicationMapper;
    private final TariffService tariffService;
    private final UsersClient usersClient;
    private final CoreClient coreClient;
    private final TransactionProducer transactionProducer;

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
    public LoanApplicationDto approveLoanApplication(LoanApplicationDecisionDto loanApplicationDecisionDto) {
        var loanApplication = findLoanApplicationForAction(loanApplicationDecisionDto.getLoanApplicationId());

        loanApplication.setState(LoanApplicationState.APPROVED);
        loanApplication.setUpdatedDateFinal(new Date());
        loanApplication.setOfficerId(loanApplicationDecisionDto.getOfficerId());

        var clientDto = usersClient.getClient(loanApplication.getClientId());
        var accountDto = coreClient.createLoanAccount(
                clientDto.getId(),
                clientDto.getFirstName(),
                clientDto.getLastName(),
                clientDto.getPatronymic(),
                loanApplication.getCurrency()
        );

        var loan = buildLoanEntity(loanApplication, clientDto, accountDto);
        loan = loanRepository.save(loan);

        var paymentTransaction = InitTransactionDto.builder()
                .payeeAccountId(loan.getAccountId())
                .amount(loan.getIssuedAmount())
                .currency(accountDto.getCurrency())
                .type(TransactionType.LOAN_PAYMENT)
                .build();

        transactionProducer.sendMessage(loan.getId(), paymentTransaction);

        return loanApplicationMapper.mapEntityToDto(loanApplication);
    }

    @Override
    @Transactional
    public LoanApplicationDto rejectLoanApplication(LoanApplicationDecisionDto loanApplicationDecisionDto) {
        var loanApplication = findLoanApplicationForAction(loanApplicationDecisionDto.getLoanApplicationId());
        loanApplication.setState(LoanApplicationState.REJECTED);
        loanApplication.setOfficerId(loanApplicationDecisionDto.getOfficerId());
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

    @Override
    @Transactional
    public LoanApplicationDto getLoanApplication(UUID loanApplicationId) {
        return loanApplicationMapper.mapEntityToDto(findLoanApplication(loanApplicationId));
    }

    private LoanApplicationEntity findLoanApplicationForAction(UUID id) {
        var loanApplication = findLoanApplication(id);

        if (loanApplication.getState() != LoanApplicationState.UNDER_CONSIDERATION) {
            throw new ConflictException(STR."Invalid loan application state for action, it must be \{LoanApplicationState.UNDER_CONSIDERATION}");
        }

        return loanApplication;
    }

    private LoanApplicationEntity findLoanApplication(UUID id) {
        return loanApplicationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(STR."LoanApplication with id \{id} not found"));
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
                .multiply(issuedAmount)
                .multiply(BigDecimal.valueOf(loanApplication.getLoanTermInDays()))
                .add(issuedAmount);

        var issuedDate = new Date();
        var calendar = Calendar.getInstance();
        calendar.setTime(issuedDate);
        calendar.add(Calendar.DAY_OF_MONTH, loanApplication.getLoanTermInDays());
        var repaymentDate = calendar.getTime();

        return LoanEntity.builder()
                .issuedDate(issuedDate)
                .repaymentDate(repaymentDate)
                .issuedAmount(loanApplication.getIssuedAmount())
                .amountLoan(amountLoan)
                .amountDebt(amountLoan)
                .accruedPenny(BigDecimal.ZERO)
                .currency(loanApplication.getCurrency())
                .loanTermInDays(loanApplication.getLoanTermInDays())
                .clientId(clientDto.getId())
                .accountId(accountDto.getId())
                .state(LoanState.PENDING)
                .tariff(loanApplication.getTariff())
                .loanApplication(loanApplication)
                .build();
    }


}
