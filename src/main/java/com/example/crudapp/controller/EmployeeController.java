package com.example.crudapp.controller;

import com.example.crudapp.Entity.Employee;
import com.example.crudapp.Entity.User;
import com.example.crudapp.service.EmployeeService;
import com.example.crudapp.service.UserService;
import jakarta.servlet.http.HttpSession;
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

        if(bindingResult.hasErrors()){
            return "employee/sign-in-form";
        }
        else if(userService.findByEmail(theUser.getEmail()) != null){
            redirectAttributes.addFlashAttribute("error", "Already Registered Please Login");
            return "redirect:/employee/login";
        }
        else{
            userService.save(theUser);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Registered Successfully!");
        return "redirect:/employee/login";
    }

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
        Employee theEmployee = employeeService.findById(id);
        theModel.addAttribute("employee",theEmployee);
        return "employee/update-form";
    }

    @PostMapping("/loginUser")
    public String loginUser(
            @ModelAttribute("user") User theUser,
            Model theModel,
            RedirectAttributes redirectAttributes, HttpSession session){
        // 1. Find user by email
        User existingUser = userService.findByEmail(theUser.getEmail());

        // 2. If not found → error
        if(existingUser == null){
            redirectAttributes.addFlashAttribute("error", "Invalid Email");
            return "redirect:/employee/login";
        }

        // 3. If found but password doesn't match → error
        if(!passwordEncoder.matches(theUser.getPassword(), existingUser.getPassword())){
            redirectAttributes.addFlashAttribute("error", "Invalid Password");
            return "redirect:/employee/login";
        }

        if(!existingUser.getRole().equals(theUser.getRole())){
            redirectAttributes.addFlashAttribute("error", "Invalid Role");
            return "redirect:/employee/login";
        }

        // 4. If both match → store role in session and redirect
        session.setAttribute("role", existingUser.getRole());
        return "redirect:/employee/list";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
      session.invalidate();
      return "redirect:/employee/login";
    }
}
