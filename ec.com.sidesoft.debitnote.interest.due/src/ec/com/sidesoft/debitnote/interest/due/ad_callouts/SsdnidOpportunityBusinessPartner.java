package ec.com.sidesoft.debitnote.interest.due.ad_callouts;

import java.math.BigDecimal;

import javax.servlet.ServletException;

import org.openbravo.dal.service.OBDal;
import org.hibernate.criterion.Restrictions;
import org.openbravo.dal.service.OBCriteria;
import org.openbravo.erpCommon.ad_callouts.SimpleCallout;
import org.openbravo.model.common.businesspartner.BusinessPartner;
import org.openbravo.model.common.businesspartner.Location;

import ec.com.sidesoft.debitnote.interest.due.SsdnidPendingInterestV;

public class SsdnidOpportunityBusinessPartner extends SimpleCallout {

  private static final long serialVersionUID = 1L;

  @Override
  protected void execute(CalloutInfo info) throws ServletException {

    try {
      String partnerId = info.getStringParameter("inpcBpartnerId", null);
      BusinessPartner partner = OBDal.getInstance().get(BusinessPartner.class, partnerId);

      if (partnerId != null && !partnerId.equals("")) {
        if (partner != null) {
          OBCriteria<Location> locationlist = OBDal.getInstance().createCriteria(Location.class);
          locationlist.add(Restrictions.eq(Location.PROPERTY_BUSINESSPARTNER, partner));
          locationlist.setMaxResults(1);
          info.addResult("inptaxBpartner", partner.getTaxID());
          info.addResult("inpcellphoneBpartner", locationlist.list().get(0).getPhone());
        } else {
          info.addResult("inptaxBpartner", null);
          info.addResult("inpcellphoneBpartner", null);
        }
      } else {
        info.addResult("inptaxBpartner", null);
        info.addResult("inpcellphoneBpartner", null);
      }

      OBCriteria<SsdnidPendingInterestV> pendingInterestList = OBDal.getInstance()
          .createCriteria(SsdnidPendingInterestV.class);
      pendingInterestList
          .add(Restrictions.eq(SsdnidPendingInterestV.PROPERTY_BUSINESSPARTNER, partner));
      if (pendingInterestList.list().size() > 0) {
        SsdnidPendingInterestV pendingInterest = pendingInterestList.list().get(0);
        if (pendingInterest.getInterestlatepayment().compareTo(BigDecimal.ZERO) > 0
            || pendingInterest.getCollectionexpenses().compareTo(BigDecimal.ZERO) > 0) {

          info.addResult("ERROR",
              "El tercero " + partner.getName() + " tiene pendiente intereses de mora ($ "
                  + pendingInterest.getInterestlatepayment().toString()
                  + ") y gastos de cobranza ($ "
                  + pendingInterest.getCollectionexpenses().toString() + ")");
        }
      }
    } catch (Exception e) {
      System.out.println("SsdnidOpportunityBusinessPartner: " + e.getMessage());
    }

  }
}
