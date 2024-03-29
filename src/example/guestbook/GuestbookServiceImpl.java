package example.guestbook;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

public class GuestbookServiceImpl implements GuestbookService {

	private static final PersistenceManagerFactory PMF = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");
	private static final Logger LOG = Logger
			.getLogger(GuestbookServiceImpl.class.getName());

	@Override
	public boolean addEntry(GuestbookEntry entry) throws Exception {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(entry);
			LOG.log(Level.INFO, "entry added.");
			return true;
		} catch (Exception e) {
			throw e;
		} finally {
			pm.close();
		}
	}

	@Override
	public boolean deleteEntry(long id) throws Exception {
		PersistenceManager pm = getPersistenceManager();
		try {
			GuestbookEntry entry = pm.getObjectById(GuestbookEntry.class, id);
			pm.deletePersistent(entry);
			return true;
		} catch (Exception e) {
			throw e;
		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GuestbookEntry> listEntry() throws Exception {
		PersistenceManager pm = getPersistenceManager();
		try {
			Query query = pm.newQuery(GuestbookEntry.class);
			query.setOrdering("date desc");
			List<GuestbookEntry> list = (List<GuestbookEntry>) query.execute();
			list.size(); // prevent lazy loading
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			pm.close();
		}
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GuestbookEntry> searchEntry(String keyword) throws Exception {
		PersistenceManager pm = getPersistenceManager();
		try {
			Query query = pm.newQuery(GuestbookEntry.class,"name == '"+keyword+"'");
			//query.setOrdering("date desc");
			List<GuestbookEntry> list = (List<GuestbookEntry>) query
					.execute();
			LOG.info("list size:"+list.size());
			list.size(); // prevent lazy loading
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			pm.close();
		}

	}

}