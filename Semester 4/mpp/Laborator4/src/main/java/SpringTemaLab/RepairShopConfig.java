package SpringTemaLab;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import SpringTemaLab.repository.ComputerRepairRequestRepository;
import SpringTemaLab.repository.ComputerRepairedFormRepository;
import SpringTemaLab.repository.jdbc.ComputerRepairRequestJdbcRepository;
import SpringTemaLab.repository.jdbc.ComputerRepairedFormJdbcRepository;
import SpringTemaLab.services.ComputerRepairServices;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class RepairShopConfig {
    @Bean
    Properties getProps() {
        Properties props = new Properties();
        try{
            System.out.println("Loading properties file...");
            props.load(new FileReader("bd.config"));
        }catch(IOException e){
            System.err.println("Configuration file bd.config not found"+e);
        }
        return props;
    }

    @Bean
    ComputerRepairRequestRepository requestsRepo(){
       return new ComputerRepairRequestJdbcRepository(getProps());

    }

    @Bean
    ComputerRepairedFormRepository formsRepo(){
        return new ComputerRepairedFormJdbcRepository(getProps());
    }

    @Bean
    ComputerRepairServices services(){
        return new ComputerRepairServices(requestsRepo(),formsRepo());
    }

}
