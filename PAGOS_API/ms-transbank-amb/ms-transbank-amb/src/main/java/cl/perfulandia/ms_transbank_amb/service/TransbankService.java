package cl.perfulandia.ms_transbank_amb.service;

import cl.perfulandia.ms_transbank_amb.model.dto.PaymentRequestDTO;
import cl.perfulandia.ms_transbank_amb.model.dto.PaymentResponseDTO;
import cl.perfulandia.ms_transbank_amb.model.dto.TransbankCreateResponse;

public interface TransbankService {
    PaymentResponseDTO createTransaction(PaymentRequestDTO request);
    String confirmTransaction(String token);
    TransbankCreateResponse queryTransaction(String token);
}
