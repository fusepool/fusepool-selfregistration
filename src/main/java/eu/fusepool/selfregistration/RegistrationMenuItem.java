package eu.fusepool.selfregistration;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.stanbol.commons.web.base.NavigationLink;

@Component
@Service(NavigationLink.class)
public class RegistrationMenuItem extends NavigationLink {
    
    public RegistrationMenuItem() {
        super("selfregistration/simple/form.html", "Example:selfregistration", "Register new user", 300);
    }
    
}
