package EECS3311.controller;

import EECS3311.Application;
import EECS3311.model.Location;
import EECS3311.model.users.Customer;
import EECS3311.model.users.User;
import EECS3311.model.users.UserType;
import EECS3311.view.pages.SignupPage;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

public class SignupPageController {
    private SignupPage signupPage;
    private Application application;

    public SignupPageController(Application application) {
        this.application = application;
        signupPage = new SignupPage(this);
        signupPage.init();
    }

    public SignupPage getSignupPage() {
        return signupPage;
    }

    public void login(MouseEvent e){
        application.gotoLoginPage();
    }

    public void signup(ActionEvent e){
        String[] values = signupPage.getValues();

        String name = values[1], email = values[0], pass = values[2], repass = values[3];
        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            signupPage.setResponseText("Please fill all blanks");
        } else if (!pass.equals(repass)) {
            signupPage.setResponseText("Password doesn't match it's re-enter");
        }else if(!application.isUniqueEmail(email, UserType.Customer)) {
            signupPage.setResponseText("There is already an account on this email");
        }else{
            Customer customer = new Customer(application.getNewUserID(UserType.Customer), name,
                    email, User.hasher.hash(pass.toCharArray()), new Location("", 0,0));
            application.getAllCustomers().add(customer);
            application.gotoCustomerHomePage(customer);
        }
    }
}
