package insuranceProduct;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class MemberPaperForm {

	private LinkedHashMap<String,String> BasicPaperList;
	private boolean approvalStatus;

	public MemberPaperForm(LinkedHashMap<String,String> BasicPaperList){
		this.BasicPaperList = BasicPaperList;
		this.approvalStatus = false;
	}
	public void approval() {
		this.approvalStatus = true;
	}
	public boolean isApproval() {
		return this.approvalStatus;
	}

	public String toString() {
		String retStr = "";
        for (Entry<String, String> entry : BasicPaperList.entrySet()) {
        	retStr += entry.getKey() + "\t: " + entry.getValue() + "\n";
        }
        return retStr;
	}
	public void finalize() throws Throwable {

	}

}