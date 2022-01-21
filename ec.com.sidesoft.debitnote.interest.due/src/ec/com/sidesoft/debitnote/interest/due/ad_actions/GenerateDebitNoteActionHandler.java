package ec.com.sidesoft.debitnote.interest.due.ad_actions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.openbravo.base.exception.OBException;
import org.openbravo.client.kernel.BaseActionHandler;
import org.openbravo.dal.core.OBContext;
import org.openbravo.database.ConnectionProvider;
import org.openbravo.model.ad.access.User;
import org.openbravo.model.common.enterprise.Organization;
import org.openbravo.service.db.DalConnectionProvider;

public class GenerateDebitNoteActionHandler extends BaseActionHandler {

  @Override
  protected JSONObject execute(Map<String, Object> parameters, String data) {
    JSONObject response = new JSONObject();
    try {
      response.put("result", "ERROR");

      final JSONObject jsonData = new JSONObject(data);
      final JSONArray rows = jsonData.getJSONArray("rows");

      String records = "";
      for (int i = 0; i < rows.length(); i++) {
        if (records.isEmpty()) {
          records += rows.getString(i);

        } else {
          records += "|" + rows.getString(i);
        }
      }

      Organization org = OBContext.getOBContext().getCurrentOrganization();
      User user = OBContext.getOBContext().getUser();

      String result = createDebitNote(org.getId(), user.getId(), records);
      System.out.println(result);

      response.put("result", "OK");
    } catch (Exception e) {
      System.out.println("GenerateDebitNoteActionHandler: " + e.getMessage());
    }
    return response;
  }

  private String createDebitNote(String orgId, String userId, String paymentDetailId) {
    String result = null;
    ConnectionProvider conn = new DalConnectionProvider(false);
    try {
      String sql = "SELECT ssdnid_createdebitnote2(?,?,?) AS result";
      PreparedStatement st = null;
      st = conn.getPreparedStatement(sql);
      st.setString(1, orgId);
      st.setString(2, userId);
      st.setString(3, paymentDetailId);
      ResultSet resultSet = st.executeQuery();

      while (resultSet.next()) {
        result = resultSet.getString("result");
      }
    } catch (Exception e) {
      throw new OBException("createDebitNote: " + e.getMessage());
    }
    return result;
  }

}
