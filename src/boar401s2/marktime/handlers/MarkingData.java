package boar401s2.marktime.handlers;

import boar401s2.marktime.storage.spreadsheet.parsers.ListParser;
import boar401s2.marktime.storage.spreadsheet.worksheet.OfflineWorksheet;

public class MarkingData {
	
	Boy boy;
	ListParser sections;
	OfflineWorksheet registerEntry;
	
	public MarkingData(Boy boy){
		this.boy = boy;
		sections = new ListParser(registerEntry);
		registerEntry = (OfflineWorksheet) boy.squad.section.company.getRegisterSpreadsheet().getWorksheet(boy.getName());
	}
	
}
