package aitho.ranim.hrms.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

public interface IEmployeeViewController {
    String activateEmployee(@PathVariable String token, Model model);
}
