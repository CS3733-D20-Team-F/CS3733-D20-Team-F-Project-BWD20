package edu.wpi.teamF.ModelClasses.UIClasses;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teamF.ModelClasses.Account.Account;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UIAccount extends RecursiveTreeObject<UIAccount> {

  public StringProperty firstName;
  public StringProperty lastName;
  public StringProperty emailAddress;
  public StringProperty userName;
  public StringProperty type;

  public UIAccount(
      String string, String string1, String string2, String string3, Account.Type anEnum) {
    this.firstName = new SimpleStringProperty(string);
    this.lastName = new SimpleStringProperty(string1);
    this.emailAddress = new SimpleStringProperty(string2);
    this.userName = new SimpleStringProperty(string3);
    this.type = new SimpleStringProperty(anEnum.toString());
  }

  public boolean equalsAccount(Account account) {
    boolean isEqual = false;
    if (account != null) {
      isEqual =
          this.firstName.get().equals(account.getFirstName())
              && this.lastName.get().equals(account.getLastName())
              && this.userName.get().equals(account.getUsername())
              && this.emailAddress.get().equals(account.getEmailAddress())
              && this.type.get().equals(account.getType().toString());
    }
    return isEqual;
  }
}
