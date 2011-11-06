package example.guestbook;

import java.util.List;

public interface GuestbookService {
	public boolean addEntry(GuestbookEntry entry) throws Exception;
	public boolean deleteEntry(long id) throws Exception;
	public List<GuestbookEntry> listEntry() throws Exception;
	public List<GuestbookEntry> searchEntry(String parameter) throws Exception;
}
