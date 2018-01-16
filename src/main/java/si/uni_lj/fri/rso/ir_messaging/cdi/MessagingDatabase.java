package si.uni_lj.fri.rso.ir_messaging.cdi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import si.uni_lj.fri.rso.ir_messaging.models.Message;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@RequestScoped
public class MessagingDatabase {
    private Logger log = LogManager.getLogger(MessagingDatabase.class.getName());

    @Inject
    private EntityManager em;

    private HttpClient httpClient = HttpClientBuilder.create().build();
    private ObjectMapper objectMapper = new ObjectMapper();


    public List<Message> getMessages() {
        TypedQuery<Message> query = em.createNamedQuery("Message.getAll", Message.class);
        return query.getResultList();
    }

    public List<Message> getMessagesFrom(String idFrom) {
        TypedQuery<Message> query = em.createNamedQuery("Message.getFrom", Message.class)
                .setParameter("idFrom", idFrom);
        return query.getResultList();
    }

    public List<Message> getMessagesTo(String idTo) {
        TypedQuery<Message> query = em.createNamedQuery("Message.getTo", Message.class)
                .setParameter("idTo", idTo);
        return query.getResultList();
    }

    public List<Message> getMessagesFilter(UriInfo uriInfo) {
        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0).build();
        return JPAUtils.queryEntities(em, Message.class, queryParameters);
    }

    public Message getMessage(String messageId, boolean includeExtended) {
        Message message = em.find(Message.class, messageId);
        if (message == null) {
            throw new NotFoundException();
        }
        if (includeExtended) {
            // nothing here yet
        }
        return message;
    }

    public Message createMessage(Message message) {
        try {
            beginTx();
            em.persist(message);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return message;
    }

    public Message putMessage(String messageId, Message message) {
        Message p = em.find(Message.class, messageId);
        if (p == null) {
            return null;
        }
        try {
            beginTx();
            message.setId(p.getId());
            message = em.merge(message);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }
        return message;
    }

    public boolean deleteMessage(String messageId) {
        Message p = em.find(Message.class, messageId);
        if (p != null) {
            try {
                beginTx();
                em.remove(p);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else {
            return false;
        }
        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
