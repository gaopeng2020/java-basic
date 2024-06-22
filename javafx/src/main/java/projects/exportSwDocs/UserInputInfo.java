package projects.exportSwDocs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserInputInfo implements Serializable {
     private  String lastOpenDir;
     private  String lastSaveDir;
}
