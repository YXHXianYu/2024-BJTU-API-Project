package com.yxhxianyu.tiktok;

import com.yxhxianyu.tiktok.pojo.UserPojo;
import com.yxhxianyu.tiktok.service.*;
import com.yxhxianyu.tiktok.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class TiktokApplicationTests {

	@Autowired
	UserService userService;

	/* Util Test */
//	@Test
	void passwordTest() {
		String password = "20021012";
		System.out.println(Util.passwordEncoder(password));
	}

//	@Test
	void userServiceShowAllUsers() {
		System.out.println(userService.getAllUsers());
	}

//	@Test
	void userServiceNullPointerException() {
		userService.deleteUserByName("alice233");
	}

}
