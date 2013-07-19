package transceiver;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class MailImap {

	String user;
	String passwd;
	
	public MailImap(String user, String passwd) {
		this.user = user;
		this.passwd = passwd;
	}
	
	public List<MailObject> getMail() throws MessagingException, IOException {
		
		String host = "imap.gmail.com";
		int port = 993;
		String target_folder = "INBOX";

		Properties props = System.getProperties();
		Session sess = Session.getInstance(props, null);
//		sess.setDebug(true);

		Store st = sess.getStore("imaps");
		st.connect(host, port, user, passwd);
		Folder fol = st.getFolder(target_folder);
		if(fol.exists()){
			for(Folder f : fol.list()){
				System.out.println(f.getName());
			}
			fol.open(Folder.READ_ONLY);
			for(Message m : fol.getMessages()){
				System.out.printf("%s - %d\n", m.getSubject(), m.getSize());
				System.out.println(m.getContent());
			}
			fol.close(false);
		}else{
			System.out.printf("%s is not exist.", target_folder);
		}
		st.close();

		return new List<MailObject>() {

			@Override
			public int size() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean contains(Object o) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Iterator<MailObject> iterator() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object[] toArray() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <T> T[] toArray(T[] a) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean add(MailObject e) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean remove(Object o) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean containsAll(Collection<?> c) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean addAll(Collection<? extends MailObject> c) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean addAll(int index, Collection<? extends MailObject> c) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean removeAll(Collection<?> c) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean retainAll(Collection<?> c) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void clear() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public MailObject get(int index) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public MailObject set(int index, MailObject element) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void add(int index, MailObject element) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public MailObject remove(int index) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int indexOf(Object o) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int lastIndexOf(Object o) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public ListIterator<MailObject> listIterator() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ListIterator<MailObject> listIterator(int index) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<MailObject> subList(int fromIndex, int toIndex) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}
