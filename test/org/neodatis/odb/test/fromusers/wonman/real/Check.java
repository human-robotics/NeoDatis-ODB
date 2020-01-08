package org.neodatis.odb.test.fromusers.wonman.real;

public interface Check {
	
	public class Max {
		public int size;
		
		public Max(int size) {
			this.size = size;
		}
	}
	
	public class Min {
		public int size;
		
		public Min(int size) {
			this.size = size;
		}
	}
	
	
	public class SameSize {
		public int size;
		
		public SameSize(int size) {
			this.size = size;
		}
	}
	
	public class EmailAddress {
		public boolean isValid(String emailAddress) {
			boolean valid = true;
			//
			
			if(emailAddress == null)
				throw new RuntimeException("Email Address was Null.");
			
			emailAddress = emailAddress.trim();

			valid = (emailAddress.length() >= 5
					&& emailAddress.contains("@")
					&& emailAddress.indexOf("@") >= 1);

			if(valid) {
				int index = emailAddress.indexOf("@");
				String temp = emailAddress.substring(index + 1);
				index = temp.indexOf(".");
				if(index > 0) {
					valid = temp.substring(index + 1).length() > 2;
				}
				else {
					valid = false;
				}
			}

			//
			return valid;
		}
	}
}
