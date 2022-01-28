package ec.com.sidesoft.debitnote.interest.due.ad_actions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;
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
  private final Logger log4j = Logger.getLogger(GenerateDebitNoteActionHandler.class);

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
      log4j.info(result);

      response.put("result", "OK");
    } catch (Exception e) {
      System.out.println("GenerateDebitNoteActionHandler: " + e.getMessage());
      log4j.error("GenerateDebitNoteActionHandler: " + e.getMessage());
    }
    return response;
  }

  private String createDebitNote(String orgId, String userId, String paymentDetailId) {
    String result = null;
    ConnectionProvider conn = new DalConnectionProvider(false);
    PreparedStatement st = null;
    try {
      String sql = "SELECT ssdnid_createdebitnote2(?,?,?) AS result";
      st = conn.getPreparedStatement(sql);
      st.setString(1, orgId);
      st.setString(2, userId);
      st.setString(3, paymentDetailId);
      ResultSet rs = st.executeQuery();

      while (rs.next()) {
        result = rs.getString("result");
      }

      rs.close();
      st.close();
    } catch (Exception e) {
      throw new OBException("createDebitNote: " + e.getMessage());
    } finally {
      try {
        conn.releasePreparedStatement(st);
        conn.destroy();
      } catch (Exception ignore) {
        ignore.printStackTrace();
      }
    }
    return result;
  }

}
