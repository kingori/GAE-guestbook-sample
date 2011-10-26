package example.guestbook;

import java.util.List;

public interface GuestbookService {
	public boolean addEntry(GuestbookEntry entry);
	public boolean deleteEntry(GuestbookEntry entry);
	public List<GuestbookEntry> listEntry() throws Exception;
}
