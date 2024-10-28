package EECS3311.controller;

import EECS3311.Application;
import EECS3311.model.users.Admin;
import EECS3311.model.users.Customer;
import EECS3311.model.users.Manager;
import EECS3311.model.users.UserType;
import EECS3311.view.pages.LoginPage;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

public class LoginPageController {
    private LoginPage loginPage;
    private Application application;

    public LoginPageController(Application application) {
        this.application = application;
        loginPage = new LoginPage(this);
        loginPage.init();
    }

    public LoginPage getLoginPage() {
        return loginPage;
    }

    public void login(ActionEvent e) {
        String[] values = loginPage.getValues();
        UserType userType = UserType.valueOf(values[0]);
        String email = values[1];
        String pass = values[2];
        if (email.isEmpty() || pass.isEmpty())
            loginPage.setResponseText("PLease fill all blanks.");
        else
            switch (userType) {
                case Manager: {
                    Manager manager = application.loginManager(email, pass);
                    if (manager == null)
                        loginPage.setResponseText("invalid manager username/password");
                    else
                        application.gotoManagerHomePage(manager);
                    break;
                }
                case Customer: {
                    Customer customer = application.loginCustomer(email, pass);
                    if (customer == null)
                        loginPage.setResponseText("invalid customer username/password");
                    else
                        application.gotoCustomerHomePage(customer);
                    break;
                }
                case Administrator: {
                    Admin admin = application.loginAdmin(email, pass);
                    if (admin == null)
                        loginPage.setResponseText("invalid admin username/password");
                    else
                        application.gotoAdminHomePage(admin);
                    break;
                }
            }
    }

    public void signup(MouseEvent e) {
        application.gotoSignupPage();
    }
}
