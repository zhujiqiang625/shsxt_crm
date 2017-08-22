package com.shsxt.crm.Task;

import com.shsxt.crm.service.CustomerLossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CustomerLossJob {
    @Autowired
    private CustomerLossService customerLossService;
    @Scheduled(cron = "0/1 * * * * ?")
    public void runLossCustomer(){
        System.out.println("每秒运行一次");
        //customerLossService.runLossCustomer();
    }
}
