package projectaurora.core;

public enum MusicType {
	DEFAULT("default"),
	VULCAN("vulcan"),
	LEPORIDAE("leporidae");
	
	public final String name;
	
	private MusicType(String cName) {
		this.name = cName;
	}
	
	public static MusicType forName(String cName) {
		for(MusicType m : values()) {
			if(cName.equals(m.name)) {
				return m;
			}
		}
		
		return null;
	}
}
