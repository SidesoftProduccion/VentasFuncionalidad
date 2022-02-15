package ec.com.sidesoft.debitnote.interest.due.ad_actions;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.criterion.Restrictions;
import org.openbravo.base.exception.OBException;
import org.openbravo.client.kernel.BaseActionHandler;
import org.openbravo.dal.service.OBCriteria;
import org.openbravo.dal.service.OBDal;
import org.openbravo.database.ConnectionProvider;
import org.openbravo.model.common.invoice.Invoice;
import org.openbravo.model.financialmgmt.payment.FIN_Payment;
import org.openbravo.model.financialmgmt.payment.FIN_PaymentDetail;
import org.openbravo.model.financialmgmt.payment.FIN_PaymentSchedule;
import org.openbravo.model.financialmgmt.payment.FIN_PaymentScheduleDetail;
import org.openbravo.service.db.DalConnectionProvider;

public class AddPaymentsActionHandler extends BaseActionHandler {
  private final Logger log4j = Logger.getLogger(AddPaymentsActionHandler.class);

  @Override
  protected JSONObject execute(Map<String, Object> parameters, String data) {
    JSONObject result = new JSONObject();

    try {
      final JSONObject jsonData = new JSONObject(data);
      final String event = jsonData.getString("event");

      String invoiceNo = jsonData.getString("invoiceNo");
      String expectedDate = jsonData.getString("expectedDate");

      Date date = new SimpleDateFormat("yyyy-MM-dd").parse(expectedDate);

      OBCriteria<Invoice> invoices = OBDal.getInstance().createCriteria(Invoice.class);
      invoices.add(Restrictions.eq(Invoice.PROPERTY_SALESTRANSACTION, true));
      invoices.add(Restrictions.eq(Invoice.PROPERTY_DOCUMENTNO, invoiceNo));

      if (invoices.list().size() == 0) {
        throw new OBException("Factura no encontrada");
      }
      Invoice invoice = invoices.list().get(0);

      OBCriteria<FIN_PaymentSchedule> pss = OBDal.getInstance()
          .createCriteria(FIN_PaymentSchedule.class);
      pss.add(Restrictions.eq(FIN_PaymentSchedule.PROPERTY_INVOICE, invoice));
      pss.add(Restrictions.eq(FIN_PaymentSchedule.PROPERTY_EXPECTEDDATE, date));

      if (pss.list().size() == 0) {
        throw new OBException("Plan de pago no encontrado");
      }
      FIN_PaymentSchedule ps = pss.list().get(0);

      switch (event) {
      case "onChangeInterestLatePayment":
        Double interestLatePayment = jsonData.getDouble("value");
        System.out.println(
            "onChangeInterestLatePayment: " + ps.getId() + " | " + interestLatePayment.toString());
        log4j.info(
            "onChangeInterestLatePayment: " + ps.getId() + " | " + interestLatePayment.toString());
        ps.setSSDNIDTInterestCollectionForLatePayment(new BigDecimal(interestLatePayment));
        break;
      case "onChangeCollectionExpenses":
        Double collectionExpenses = jsonData.getDouble("value");
        System.out.println(
            "onChangeCollectionExpenses: " + ps.getId() + " | " + collectionExpenses.toString());
        log4j.info(
            "onChangeCollectionExpenses: " + ps.getId() + " | " + collectionExpenses.toString());
        ps.setSSDNIDTCollectionInterestfForCollection(new BigDecimal(collectionExpenses));
        break;
      case "onChangeGeneratePayment":
        Boolean generatePayment = jsonData.getBoolean("value");
        System.out
            .println("onChangeGeneratePayment: " + ps.getId() + " | " + generatePayment.toString());
        log4j.info("onChangeGeneratePayment: " + ps.getId() + " | " + generatePayment.toString());
        ps.setSSDNIDGeneratePayment(generatePayment);
        break;
      default:
        break;
      }

      OBDal.getInstance().save(ps);
      OBDal.getInstance().flush();
      result.put("status", "OK");
    } catch (

    Exception e) {
      System.out.println("DocumentationActionHandler: " + e.getMessage());
      log4j.error("DocumentationActionHandler: " + e.getMessage());
      try {
        result.put("status", "ERROR");
        result.put("message", e.getMessage());
      } catch (Exception ex) {
      }
    }
    return result;
  }

}
