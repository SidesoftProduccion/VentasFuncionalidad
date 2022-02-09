package ec.com.sidesoft.debitnote.interest.due;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.openbravo.client.kernel.BaseComponentProvider;
import org.openbravo.client.kernel.Component;
import org.openbravo.client.kernel.ComponentProvider;

@ApplicationScoped
@ComponentProvider.Qualifier(SsdnidComponentProvider.QUALIFIER)
public class SsdnidComponentProvider extends BaseComponentProvider {
  public static final String QUALIFIER = "SSDNID_ComponentProvider";

  @Override
  public Component getComponent(String componentId, Map<String, Object> parameters) {
    throw new IllegalArgumentException("Component id " + componentId + " not supported.");
  }

  @Override
  public List<ComponentResource> getGlobalComponentResources() {

    final List<ComponentResource> globalResources = new ArrayList<ComponentResource>();
    final String prefix = "web/ec.com.sidesoft.debitnote.interest.due";

    globalResources.add(createStaticResource(prefix + "/js/onChange.js", false));
    globalResources.add(createStaticResource(prefix + "/js/generateDebitNote.js", false));

    return globalResources;
  }

}