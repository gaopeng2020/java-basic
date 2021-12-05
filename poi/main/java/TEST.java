import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

import java.io.*;
import java.math.BigInteger;
import java.util.List;

public class TEST {
    public static void main(String[] args) {

        String workDir = "D:\\Users\\Lenovo\\Documents\\gitlab\\java\\poi\\main\\resources";
        String wordName = "01_Ethernet PHY Specification_V1.0_中英.docx";
        File file = new File(workDir + "\\" + wordName);

        InputStream is = null;
        XWPFDocument docx = null;

        OutputStream out = null;
        String[] title = new String[276];
        String[] concent = new String[276];
        String[] type = new String[276];
        int i = 0;
        try {
            XWPFDocument createDoc = new XWPFDocument();

            is = new FileInputStream(file);
            docx = new XWPFDocument(is);

            //获取段落
            List<XWPFParagraph> paras = docx.getParagraphs();

            //获取样式
            final XWPFStyles styles = docx.getStyles();

            for (XWPFParagraph para : paras) {
                //得到xml格式
                final CTP ctp = para.getCTP();
//                System.out.println(ctp);

                final CTPPr pPr = ctp.getPPr();
//                System.out.println("pPr = " + pPr);

                //段落内容
                final String paragraphText = para.getParagraphText();

                //段落样式
                final String paraStyle = para.getStyle();

                final String titleLvl = WordHandler.getTitleLvl(docx, para);
                System.out.println("titleLvl = " + titleLvl);


//                System.out.println("paraStyle = " + paraStyle);
//                System.out.println("-------------------------------------------------");
/*                final CTStyle paraCtStyle = styles.getStyle(paraStyle).getCTStyle();
                final String paraStyleName = paraCtStyle.getBasedOn().getVal();

                if (pPr.getOutlineLvl() != null) {
                    final CTDecimalNumber outlineLvl1 = pPr.getOutlineLvl();
                    System.out.println("OutlineLvlVal = " + outlineLvl1.getVal());
                    System.out.println("paragraphText = " + paragraphText);

                } else if (styles.getStyle(paraStyle).getCTStyle().getPPr().getOutlineLvl() != null) {
                    System.out.println("paraStyle = " + paraStyle);
                    final BigInteger OutlineLvlVal = paraCtStyle.getPPr().getOutlineLvl().getVal();
                    System.out.println("OutlineLvlVal = " + OutlineLvlVal);

                    System.out.println("paragraphText = " + paragraphText);

                } else if (styles.getStyle(styles.getStyle(paraStyle).getCTStyle().getBasedOn().getVal()).getCTStyle().getPPr().getOutlineLvl() != null) {
                    final CTDecimalNumber outlineLvl = styles.getStyle(paraStyleName).getCTStyle().getPPr().getOutlineLvl();
                    System.out.println("OutlineLvlVal = " + outlineLvl.getVal());

                    System.out.println("paragraphText = " + paragraphText);
                }else{

                }*/
                //段落级别
//                System.out.println(para.getStyleID());

                /*String titleLvl = WordHandler.getTitleLvl(doc, para);//获取段落级别
                if ("a5".equals(titleLvl) || "HTML".equals(titleLvl) || "".equals(titleLvl) || null == titleLvl) {
                    titleLvl = "8";
                }
                //System.out.println(titleLvl+"-----");//0,1,2
                if (!"8".equals(titleLvl)) {
                    //System.out.println(titleLvl+"===="+para.getParagraphText());

                    if ("3".equals(titleLvl)) {

                        if (concent[i] != null) {
                            concent[i] = concent[i] + para.getParagraphText();
                        } else {
                            concent[i] = para.getParagraphText();
                        }
                        //System.out.println(concent[i]);
                    }
                    if ("2".equals(titleLvl)) {
                        i++;
                        title[i] = para.getParagraphText();
                        type[i] = type[i - 1];
                        //System.out.println(title[i]);
                    }
                    if ("1".equals(titleLvl)) {
                        i++;
                        type[i] = para.getParagraphText();
                        //System.out.println(title[i]);
                    }
                }*/
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
