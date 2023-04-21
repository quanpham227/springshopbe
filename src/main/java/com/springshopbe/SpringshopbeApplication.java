package com.springshopbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import java.util.Date;

@SpringBootApplication
@EnableTransactionManagement
public class SpringshopbeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringshopbeApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
//    modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
		modelMapper.createTypeMap(String.class, Date.class);
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		return modelMapper;
	}
}
