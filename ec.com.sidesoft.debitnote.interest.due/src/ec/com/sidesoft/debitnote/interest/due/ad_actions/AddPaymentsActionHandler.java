package ec.com.sidesoft.debitnote.interest.due.ad_actions;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.openbravo.base.exception.OBException;
import org.openbravo.client.kernel.BaseActionHandler;
import org.openbravo.dal.service.OBDal;
import org.openbravo.database.ConnectionProvider;
import org.openbravo.model.financialmgmt.payment.FIN_Payment;
import org.openbravo.model.financialmgmt.payment.FIN_PaymentDetail;
import org.openbravo.model.financialmgmt.payment.FIN_PaymentSchedule;
import org.openbravo.model.financialmgmt.payment.FIN_PaymentScheduleDetail;
import org.openbravo.service.db.DalConnectionProvider;

public class AddPaymentsActionHandler extends BaseActionHandler {

  @Override
  protected JSONObject execute(Map<String, Object> parameters, String data) {
    JSONObject result = new JSONObject();

    try {
      final JSONObject jsonData = new JSONObject(data);
      final String event = jsonData.getString("event");

      String paymentScheduleDetailId = jsonData.getString("paymentScheduleDetailId");
      FIN_PaymentScheduleDetail psd = OBDal.getInstance().get(FIN_PaymentScheduleDetail.class,
          paymentScheduleDetailId);
      if (psd == null) {
        throw new OBException("Detalle de pago no encontrado");
      }
      FIN_PaymentSchedule ps = psd.getInvoicePaymentSchedule();

      switch (event) {
      case "onChangeInterestLatePayment":
        Double interestLatePayment = jsonData.getDouble("value");
        System.out.println(
            "onChangeInterestLatePayment: " + ps.getId() + " | " + interestLatePayment.toString());
        ps.setSSDNIDTInterestCollectionForLatePayment(new BigDecimal(interestLatePayment));
        break;
      case "onChangeCollectionExpenses":
        Double collectionExpenses = jsonData.getDouble("value");
        System.out.println(
            "onChangeCollectionExpenses: " + ps.getId() + " | " + collectionExpenses.toString());
        ps.setSSDNIDTCollectionInterestfForCollection(new BigDecimal(collectionExpenses));
        break;
      case "onChangeGeneratePayment":
        Boolean generatePayment = jsonData.getBoolean("value");
        System.out
            .println("onChangeGeneratePayment: " + ps.getId() + " | " + generatePayment.toString());
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
      try {
        result.put("status", "ERROR");
        result.put("message", e.getMessage());
      } catch (Exception ex) {
      }
    }
    return result;
  }

}
