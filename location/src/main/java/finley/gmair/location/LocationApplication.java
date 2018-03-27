package finley.gmair.location;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import finley.gmair.model.district.City;
import finley.gmair.model.district.District;
import finley.gmair.model.district.Province;
import finley.gmair.service.LocationService;
import finley.gmair.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@ComponentScan({"finley.gmair.service", "finley.gmair.dao"})
@RequestMapping("/location")
public class LocationApplication {
    private Logger logger = LoggerFactory.getLogger(LocationApplication.class);

    private final static String TENCENT_DISTRICT_URL = "http://apis.map.qq.com/ws/district/v1/list";

    @Autowired
    private LocationService locationService;

    public static void main(String[] args) {
        SpringApplication.run(LocationApplication.class, args);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/address/resolve")
    public ResultData geocoder(String address) {
        ResultData result = new ResultData();

        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/init")
    public ResultData init() {
        ResultData result = new ResultData();
        String url = new StringBuffer(TENCENT_DISTRICT_URL).append("?key=").append(LocationProperties.getValue("tencent_map_key")).toString();
        try {
            JSONObject response = JSON.parseObject(HttpDeal.getResponse(url));
            if (!StringUtils.isEmpty(response) && response.getInteger("status") == 0) {
                JSONArray data = response.getJSONArray("result");
                JSONArray provinces = data.getJSONArray(0);
                JSONArray cities = data.getJSONArray(1);
                JSONArray districts = data.getJSONArray(2);
                JSONObject province, city, district;
                boolean flag = false;
                int i, j, k;
                for (i = 0, j = 0, k = 0; k < districts.size(); k++) {
                    //assign the province, city and district
                    province = provinces.getJSONObject(i);
                    city = cities.getJSONObject(j);
                    district = districts.getJSONObject(k);
                    if (i == 0 && j == 0 && k == 0) {
                        locationService.createProvince(new Province(province));
                        locationService.createCity(new City(city), province.getString("id"));
                    }
                    int pEnd = province.getJSONArray("cidx").getIntValue(1);
                    //judge whether the province is a province-level municipality
                    if (!city.containsKey("cidx")) {
                        flag = true;
                        if (j < pEnd + 1) {
                            if (j != 0) locationService.createCity(new City(city), province.getString("id"));
                            j++;
                            k--;
                            continue;
                        }
                        province = provinces.getJSONObject(++i);
                        //move to the next province
                        locationService.createProvince(new Province(province));
                        k--;
                        continue;
                    }
                    if (flag) {
                        if (j >= pEnd + 1) {
                            province = provinces.getJSONObject(++i);
                            locationService.createProvince(new Province(province));
                        }
                        locationService.createCity(new City(city), province.getString("id"));
                        flag = false;
                    }
                    //judge whether the city exist in city list
                    int cEnd = city.getJSONArray("cidx").getIntValue(1);
                    //if out of city range but still in province range, read the next city
                    if (k < cEnd + 1) {
                        //the district still belongs to the current city
                        locationService.createDistrict(new District(district), city.getString("id"));
                        continue;
                    }
                    city = cities.getJSONObject(++j);
                    if (j < pEnd + 1) {
                        //the city still belongs to the current province
                        locationService.createCity(new City(city), province.getString("id"));
                        k--;
                        continue;
                    }
                    province = provinces.getJSONObject(++i);
                    //move to the next province
                    locationService.createProvince(new Province(province));
                    locationService.createCity(new City(city), province.getString("id"));
                    k--;
                }
                for (; j < cities.size(); j++) {
                    province = provinces.getJSONObject(i);
                    city = cities.getJSONObject(j);
                    int pEnd = province.getJSONArray("cidx").getIntValue(1);
                    if (j < pEnd + 1) {
                        locationService.createCity(new City(city), province.getString("id"));
                        continue;
                    }
                    province = provinces.getJSONObject(++i);
                    j--;
                    //move to the next province
                    locationService.createProvince(new Province(province));
                }
                for (; i < provinces.size(); i++) {
                    province = provinces.getJSONObject(i);
                    locationService.createProvince(new Province(province));
                }
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to retrieve the response from tencent map");
            }
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
