package jp.co.internous.ecsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.internous.ecsite.model.domain.MstUser;
import jp.co.internous.ecsite.model.form.SignupForm;
import jp.co.internous.ecsite.model.mapper.MstUserMapper;

@Controller
@RequestMapping("/ecsite/signup")
public class SignupController {
	
	@Autowired
	private MstUserMapper userMapper;

	@RequestMapping
	public String index(Model model) {
		model.addAttribute("signupForm", new SignupForm());
		return "signup";
	}
	
    @PostMapping("/signupUser")
    public String signup(SignupForm signupForm, Model model, RedirectAttributes redirectAttributes) {
        // 入力エラーチェック
        if (signupForm.getFullName() == null || signupForm.getFullName().isEmpty() ||
            signupForm.getUserName() == null || signupForm.getUserName().isEmpty() ||
            signupForm.getPassword() == null || signupForm.getPassword().isEmpty()) {

            // エラーメッセージと入力内容をリダイレクト先に渡す
            redirectAttributes.addFlashAttribute("errMessage", "全ての項目を入力してください。");
            redirectAttributes.addFlashAttribute("signupForm", signupForm);

            // リダイレクトしてURLを「/ecsite/register」に変更
            return "redirect:/ecsite/register";
        }

        // ユーザー名の重複チェック
        MstUser existingUser = userMapper.findByUserName(signupForm.getUserName());
        if (existingUser != null) {
            redirectAttributes.addFlashAttribute("errMessage", "そのユーザー名はすでに使用されています。");
            redirectAttributes.addFlashAttribute("signupForm", signupForm);

            return "redirect:/ecsite/register";
        }

        // 新規ユーザー登録
        MstUser signupUser = new MstUser();
        signupUser.setUserName(signupForm.getUserName());
        signupUser.setPassword(signupForm.getPassword());
        signupUser.setFullName(signupForm.getFullName());

        userMapper.insert(signupUser);
        return "redirect:/ecsite/";
    }

}

