package fraj.lab.recruit.test2.atm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ATMTest {

    private static final int MONTANT_EXAMPLE = 200;
    public static final int MONTANT_NEGATIF = -115;
    @Mock
    AmountSelector amountSelector;
    @Mock
    CashManager cashManager;
    @Mock
    PaymentProcessor paymentProcessor;

    @InjectMocks
    ATM atm;


    @Test
    void given_amount_zero_then_throw_exception() {
        //when
        when(amountSelector.selectAmount()).thenReturn(0);
        //then
        Assertions.assertThrows(ATMTechnicalException.class, () -> atm.runCashWithdrawal());
    }

    @Test
    void given_amount_negative_int_then_throw_exception() {
        //when
        when(amountSelector.selectAmount()).thenReturn(MONTANT_NEGATIF);
        //then
        Assertions.assertThrows(ATMTechnicalException.class, () -> atm.runCashWithdrawal());
    }

    @Test
    void given_montant_not_available_in_atm_then_return_cash_not_available() throws ATMTechnicalException {
        //when
        when(amountSelector.selectAmount()).thenReturn(MONTANT_EXAMPLE);
        when(cashManager.canDeliver(MONTANT_EXAMPLE)).thenReturn(false);
        //then
        ATMStatus atmStatus = atm.runCashWithdrawal();

        Assertions.assertEquals(ATMStatus.CASH_NOT_AVAILABLE, atmStatus);
        verify(paymentProcessor, never()).pay(MONTANT_EXAMPLE);
        verify(cashManager, never()).deliver(MONTANT_EXAMPLE);
    }

    @Test
    void given_montant_available_in_atm_but_an_error_occured_when_payment_then_return_payment_rejected() throws ATMTechnicalException {
        //when
        when(amountSelector.selectAmount()).thenReturn(MONTANT_EXAMPLE);
        when(cashManager.canDeliver(MONTANT_EXAMPLE)).thenReturn(true);
        when(paymentProcessor.pay(MONTANT_EXAMPLE)).thenReturn(PaymentStatus.FAILURE);
        //then
        ATMStatus atmStatus = atm.runCashWithdrawal();

        Assertions.assertEquals(ATMStatus.PAYMENT_REJECTED, atmStatus);
        verify(cashManager, never()).deliver(MONTANT_EXAMPLE);
    }

    @Test
    void given_montant_available_in_atm_then_return_done() throws ATMTechnicalException {
        //when
        when(amountSelector.selectAmount()).thenReturn(MONTANT_EXAMPLE);
        when(cashManager.canDeliver(MONTANT_EXAMPLE)).thenReturn(true);
        when(paymentProcessor.pay(MONTANT_EXAMPLE)).thenReturn(PaymentStatus.SUCCESS);
        //then
        ATMStatus atmStatus = atm.runCashWithdrawal();

        Assertions.assertEquals(ATMStatus.DONE, atmStatus);
        verify(cashManager).deliver(MONTANT_EXAMPLE);
    }

}
