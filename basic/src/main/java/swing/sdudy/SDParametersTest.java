/**
 * 
 */
package swing.sdudy;

/**
 * @author Administrator
 *
 */
public class SDParametersTest {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SDParameters dialog = null;
		try {
			dialog = new SDParameters();
			dialog.setVisible(true);
			dialog.setModal(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		long startTime = System.currentTimeMillis();
		while(true)	{
			System.out.println("");
			if (dialog!=null && dialog.majorVersion_P!=null) {
				break;
			}
			//The Loop will exit after 30s
			long endTime = System.currentTimeMillis();
		      if ((endTime - startTime) / 1000 > 30) {
		        break;
		      }
		}
		
		System.out.println("majorVersion_P = "+dialog.majorVersion_P);
	}
}
