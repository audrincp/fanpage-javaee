package corejsf.modules;

public class Item {
	private int id;
	/**
	 * порядок значений такой же, как в таблице
	 */
	private String[] publicValues;
	/**
	 * порядок значений такой же, как форме редактирования
	 */
	private String[] editValues;
	public Item () {
		id = 0;
		publicValues = null;
		editValues = null;
	}
	public Item (int id, int n, int m) {
		this.id = id;
		publicValues = new String[n];
        editValues = new String[m];
	}
	public String getId () {
		return id + "";
	}
	public String getPublicValue (int index) {
		return publicValues[index];
	}
    public void setPublicValue (int index, String value) {
		publicValues[index] = value;
	}
    public String getEditValue (int index) {
		return editValues[index];
	}
    public void setEditValue (int index, String value) {
		editValues[index] = value;
	}
	public static Item[] ObjectsToItems (Object[] objects) {
		Item[] items = new Item[objects.length];
		for (int i = 0; i < objects.length; ++ i)
			items[i] = (Item)objects[i];
		return items;
	}
}