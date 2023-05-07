package dds.monedero.model;

import static org.junit.jupiter.api.Assertions.*;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void deberiaDepositarMontoSiEsPositivo() {
    cuenta.poner(1500);
    assertEquals(1500, cuenta.getSaldo());
  }

  @Test
  void noDeberiaDepositarMontoSiEsNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void noDeberiaPermitirHacerMasDe3DepositosDiarios() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
      cuenta.poner(1500);
      cuenta.poner(456);
      cuenta.poner(1900);
      cuenta.poner(245);
    });
  }

  @Test
  void deberiaPermitirHasta3Depositos() {
    assertDoesNotThrow(() -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
    });
  }

  @Test
  void noDeberiaPermitirExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.sacar(1001);
    });
  }

  @Test
  public void noDeberiaPermitirExtraerMasDeMilPorDia() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(1001);
    });
  }

  @Test
  public void noDeberiaPermitirExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

  @Test
  void deberiaValidarElMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.validarMontoIngresado(-500));
  }

  @Test
  void deberiaValidarElMontoPositivo() {
    assertDoesNotThrow(() -> cuenta.validarMontoIngresado(500));
  }

  @Test
  void deberiaCalcularLoExtraidoEnElDia() {
    cuenta.poner(500);
    cuenta.sacar(100);
    cuenta.sacar(100);
    assertEquals(200, cuenta.getMontoExtraidoA(LocalDate.now()));
  }

}