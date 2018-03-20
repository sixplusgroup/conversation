package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.district.City;
import finley.gmair.model.district.District;
import finley.gmair.model.district.Province;
import finley.gmair.service.DistrictDivisionService;
import finley.gmair.util.HttpDeal;
import finley.gmair.util.ManagementConfig;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/area")
public class DistrictController {

    @Autowired
    private DistrictDivisionService districtDivisionService;

    @RequestMapping(method = RequestMethod.GET, value = "/division")
    public ResultData create() {
        ResultData result = new ResultData();

        String url = new StringBuffer
                ("http://apis.map.qq.com/ws/district/v1/list").append("?key=")
                .append(ManagementConfig.getValue("tencent_map_key")).toString();

        try {
            String response = HttpDeal.getResponse(url);
            JSONObject jsonObject = JSON.parseObject(response);

            if (!StringUtils.isEmpty(jsonObject)) {
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                Province provinceItem = new Province();
                List<Province> provinceList = new ArrayList<>();
                City cityItem = new City();
                List<City> cityList = new ArrayList<>();
                District districtItem = new District();
                List<District> districtList = new ArrayList<>();
                for (int i = 0; i < jsonArray.getJSONArray(0).size(); i++) {
                    int startIndex = -1;
                    int endIndex = -1;
                    String provinceId = jsonArray.getJSONArray(0).getJSONObject(i).get("id").toString();
                    provinceItem.setProvinceId(provinceId);
                    provinceItem.setProvinceName(jsonArray.getJSONArray(0).getJSONObject(i).get("fullname").toString());
                    JSONArray jsonArrayPinYin= (JSONArray) jsonArray.getJSONArray(0).getJSONObject(i).get("pinyin");
                    String prostr = null;
                    for (int j = 0;j < jsonArrayPinYin.size(); j++) {
                        prostr += jsonArrayPinYin.get(j).toString();
                    }
                    provinceItem.setProvincePinyin(prostr);
                    provinceList.add(provinceItem);

                    //获取省的城市的区间下标
                    JSONArray jsonArrayCIDX= (JSONArray) jsonArray.getJSONArray(0).getJSONObject(i).get("cidx");
                    for (int j = 0; j < jsonArrayCIDX.size(); j++) {
                        if (j == 0)
                            startIndex = (int) jsonArrayCIDX.get(j);
                        if (j == 1)
                            endIndex = (int) jsonArrayCIDX.get(j);
                    }
                    //通过获得的城市的区间下标，遍历省的城市
                    for (int p = startIndex; p <= endIndex; p++) {
                        String cityId = jsonArray.getJSONArray(1).getJSONObject(p).get("id").toString();
                        cityItem.setCityId(cityId);
                        cityItem.setProvinceId(provinceId);
                        cityItem.setCityName(jsonArray.getJSONArray(1).getJSONObject(p).get("fullname").toString());
                        JSONArray jsonArrayCityPinYin= (JSONArray) jsonArray.getJSONArray(1).getJSONObject(p).get("pinyin");
                        String citystr = null;
                        for (int j = 0; j < jsonArrayCityPinYin.size(); j++){
                            citystr += jsonArrayCityPinYin.get(j).toString();
                        }
                        cityItem.setCityPinyin(citystr);
                        cityList.add(cityItem);

                        int startAreaIndex = -1;
                        int endAreaIndex = -1;
                        //获取城市的区的区间下标
                        JSONArray jsonArrayAreaCIDX = (JSONArray) jsonArray.getJSONArray(1).getJSONObject(p).get("cidx");
                        for (int j = 0; j < jsonArrayAreaCIDX.size(); j++) {
                            if (j == 0) {
                                if (jsonArrayAreaCIDX.get(j) != null)
                                    startAreaIndex = (int) jsonArrayAreaCIDX.get(j);
                            }
                            if (j == 1) {
                                if (jsonArrayAreaCIDX.get(j) != null)
                                    endAreaIndex = (int) jsonArrayAreaCIDX.get(j);
                            }
                        }
                        //通过获得的区的区间下标，遍历城市的区
                        if (startAreaIndex != -1 && endAreaIndex != -1) {
                            for (int k = startAreaIndex; k <= endAreaIndex; k++) {
                                districtItem.setDistrictId(jsonArray.getJSONArray(2).getJSONObject(k).get("id").toString());
                                districtItem.setCityId(cityId);
                                districtItem.setDistrictName(jsonArray.getJSONArray(2).getJSONObject(k).get("fullname").toString());
                                districtList.add(districtItem);
                            }
                        }

                    }
                }

                //省插入
                ResultData rs = districtDivisionService.createProvince(provinceList);
                if (rs.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    result.setData(rs.getData());
                } else {
                    result.setDescription(rs.getDescription());
                }

                //市插入
                rs = districtDivisionService.createCity(cityList);
                if (rs.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    result.setData(rs.getData());
                } else {
                    result.setDescription(rs.getDescription());
                }

                //区插入
                rs = districtDivisionService.createDistrict(districtList);
                if (rs.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    result.setData(rs.getData());
                } else {
                    result.setDescription(rs.getDescription());
                }
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("服务器繁忙，请稍后重试！");
            }
        } catch (Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}