package eu.fusepool.selfregistration;

import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.apache.clerezza.platform.usermanager.UserManager;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.stanbol.commons.security.UserUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Uses the SiteManager to resolve entities. Every requested is recorded to a
 * graph. The client gets information and meta-information about the resource
 * and sees all previous requests for that resource.
 */
@Component
@Service(Object.class)
@Property(name = "javax.ws.rs", boolValue = true)
@Path("selfregistration")
public class Registrar {

    /**
     * Using slf4j for normal logging
     */
    private static final Logger log = LoggerFactory.getLogger(Registrar.class);
    @Reference
    private UserManager userManager;
    
    @Activate
    protected void activate(ComponentContext context) {
    }
    
    @Deactivate
    protected void deactivate(ComponentContext context) {
    }

    /**
     * This method return a 201 Response if the user was created, a 400response
     * otherwise
     */
    @POST
    public Response createUser(@FormParam("userName") final String userName,
            @FormParam("password") final String password,
            @FormParam("email") final String email) throws Exception {
        return AccessController.doPrivileged(new PrivilegedAction<Response>() {
            public Response run() {
                if (userName.equals("") || userName.equals("anonymous") || userName.contains(" ") ) {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Username").build();
                }
                userManager.storeUser(userName, email.trim().equals("") ? null : email, password, (List<String>) Collections.EMPTY_LIST, null);
                return Response.noContent().build();
            }
        });    
    }
    
    @GET
    @Path("currentUser")
    public String getCurrentUser() {
        return UserUtil.getCurrentUserName();
    }
    
    @GET
    @Path("forceNonAnonymous")
    public Response forceNonAnonymous() {
        if (UserUtil.getCurrentUserName().equals("anonymous")) {
            throw new AccessControlException("must not be anonymous");
        }
        return Response.noContent().build(); 
    }
}
