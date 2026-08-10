package com.example.crudapp.controller;

import com.example.crudapp.Entity.Employee;
import com.example.crudapp.Entity.User;
import com.example.crudapp.service.EmployeeService;
import com.example.crudapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private UserService userService;
    private EmployeeService employeeService;
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public EmployeeController(UserService userService, EmployeeService employeeService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String signIn(Model theModel){
        User theUser = new User();
        theModel.addAttribute("user",theUser);
        return "employee/sign-in-form";
    }
    @GetMapping("/login")
    public String Login(Model theModel){
        User theUser = new User();
        theModel.addAttribute("user", theUser);
        return "employee/login-form";
    }

    @PostMapping("/registerUser")
    public String registerUser(@Valid @ModelAttribute("user") User theUser, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors() || theUser.getRole() == null || theUser.getRole().isEmpty()) {
            // Show error message
            redirectAttributes.addFlashAttribute("error", "Role selection is required");
            return "redirect:/employee/register";
        }

        // check if the user is already registered
        if(userService.findByEmail(theUser.getEmail()) != null){
            // RedirectAttributes is a Spring MVC interface used to pass data across a redirect without losing it.
            redirectAttributes.addFlashAttribute("error", "Already Registered Please Login");
            // The redirect: prefix is a special keyword Spring recognizes,
            // telling it to send an HTTP 302 redirect to /employee/login instead of rendering a view template.
            return "redirect:/employee/login";
        }

        else if("ADMIN".equals(theUser.getRole())){
            // Query database to see if any ADMIN user already exists
            List<User> admins = userService.findAll().stream()
                    .filter(u -> "ADMIN".equals(u.getRole()))
                    .toList();

            if(!admins.isEmpty()){
                redirectAttributes.addFlashAttribute("error","An Admin already exists");
                return "redirect:/employee/register";
            }
        }

        else{
            userService.save(theUser);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Registered Successfully!");
        return "redirect:/employee/login";
    }
    // A note on redirectAttributes :-
    // Spring temporarily stores your flash attribute in the HTTP session,
    // then after the redirect happens on the next GET request,
    // it automatically moves it into the Model and deletes it from the session.
    // So the message shows exactly once — no matter how many times the user refreshes, it won't reappear

    @GetMapping("/list")
    public String employeeList(@ModelAttribute("user") User theUser, Model theModel, RedirectAttributes redirectAttributes){
        List<Employee> theEmployees = employeeService.findAll();
        theModel.addAttribute("employees", theEmployees);
        return "employee/employee-list";
    }

    @GetMapping("/deleteEmployee")
    public String deleteEmployee(@RequestParam("employeeId") int id){
        employeeService.delete(id);
        return "redirect:/employee/list";
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute("employee") Employee theEmployee){
        employeeService.save(theEmployee);
        return "redirect:/employee/list";
    }

    @GetMapping("/showFormAdd")
    public String showFormAdd(Model theModel){
        Employee theEmployee = new Employee();
        theModel.addAttribute("employee", theEmployee);
        return "employee/update-form";
    }

    @GetMapping("/showFormUpdate")
    public String showFormUpdate(@RequestParam("employeeId") int id,Model theModel){

        // First find the employee by id
        Employee theEmployee = employeeService.findById(id);

        // then pass that employee that is found by id to the model
        theModel.addAttribute("employee",theEmployee);
        return "employee/update-form";
    }
}
