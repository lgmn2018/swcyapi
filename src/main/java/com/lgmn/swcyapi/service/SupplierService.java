package com.lgmn.swcyapi.service;

import com.lgmn.common.domain.LgmnPage;
import com.lgmn.common.domain.LgmnUserInfo;
import com.lgmn.common.result.Result;
import com.lgmn.common.result.ResultEnum;
import com.lgmn.common.utils.ObjectTransfer;
import com.lgmn.swcy.basic.entity.*;
import com.lgmn.swcyapi.dto.store.LeagueStoreGetSupplierDto;
import com.lgmn.swcyapi.dto.supplier.*;
import com.lgmn.swcyapi.service.address.SwcyReceivingAddressApiService;
import com.lgmn.swcyapi.service.assets.AssetsService;
import com.lgmn.swcyapi.service.commodity.SwcyCommodityApiService;
import com.lgmn.swcyapi.service.commodity.SwcyCommodityTypeApiService;
import com.lgmn.swcyapi.service.flow.SwcyFlowApiService;
import com.lgmn.swcyapi.service.industry.IndustryService;
import com.lgmn.swcyapi.service.store.SStoreService;
import com.lgmn.swcyapi.service.supplier.*;
import com.lgmn.swcyapi.vo.order.ReceivingAddressVo;
import com.lgmn.swcyapi.vo.supplier.*;
import org.nutz.lang.util.NutMap;
import org.nutz.weixin.util.WxPaySign;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.lgmn.swcyapi.service.OrderService.*;

@Component
public class SupplierService {

    @Autowired
    IndustryService industryService;

    @Autowired
    SwcySupplierAPIService swcySupplierAPIService;

    @Autowired
    SwcySupplierCommodityAPIService swcySupplierCommodityAPIService;

    @Autowired
    SwcySupplierCategoryAPIService swcySupplierCategoryAPIService;

    @Autowired
    SwcyReceivingAddressApiService swcyReceivingAddressApiService;

    @Autowired
    SwcySupplierOrderAPIService swcySupplierOrderAPIService;

    @Autowired
    SStoreService sStoreService;

    @Autowired
    SwcySupplierOrderDetailAPIService swcySupplierOrderDetailAPIService;

    @Autowired
    OrderService orderService;

    @Autowired
    SwcyFlowApiService swcyFlowApiService;

    @Autowired
    SwcyCommodityTypeApiService swcyCommodityTypeApiService;

    @Autowired
    SwcyCommodityApiService swcyCommodityApiService;

    @Autowired
    AssetsService assetsService;

    @Value("${wxparame.appid}")
    private String appid;

    @Value("${wxparame.mchid}")
    private String mchid;

    @Value("${wxparame.key}")
    private String key;

    @Value("${wxparame.supplier_notifyurl}")
    private String supplierNotifyurl;

    public Result getSupplierPage() throws Exception {
        List<SwcyIndustryEntity> swcyIndustryEntities = industryService.getIndustryList();
        swcyIndustryEntities = industryService.getIndustryAll(swcyIndustryEntities);
        List<GetSupplierPageVo> list = new ArrayList<>();
        List<Integer> tempIds = new ArrayList<>();
        for (int i = 0; i < swcyIndustryEntities.size(); i++) {
            GetSupplierPageVo getSupplierPageVo = new GetSupplierPageVo().getVo(swcyIndustryEntities.get(i), GetSupplierPageVo.class);
            LgmnPage<SwcySupplierEntity> swcySupplierEntityLgmnPage;
            if (swcyIndustryEntities.get(i).getId() == 0) {
                swcySupplierEntityLgmnPage = swcySupplierAPIService.getSupplierPageByIndustryId(tempIds, 0, 10);
            } else {
                List<Integer> industryIds = new ArrayList<>();
                industryIds.add(swcyIndustryEntities.get(i).getId());
                swcySupplierEntityLgmnPage = swcySupplierAPIService.getSupplierPageByIndustryId(industryIds, 0, 10);
            }
            LgmnPage<SupplierInfoVo> supplierInfoVoLgmnPage = new SupplierInfoVo().getVoPage(swcySupplierEntityLgmnPage, SupplierInfoVo.class);
            getSupplierPageVo.setInfoVoLgmnPage(supplierInfoVoLgmnPage);

            if (swcyIndustryEntities.get(i).getId() != 0) {
                list.add(getSupplierPageVo);
            } else {
                list.add(0, getSupplierPageVo);
            }
            tempIds.add(swcyIndustryEntities.get(i).getId());
        }
        return Result.success(list);
    }

    public Result loadMoreSupplierPage(LoadMoreSupplierPageDto loadMoreSupplierPageDto) throws Exception {
        List<Integer> industryIds = new ArrayList<>();
        if (loadMoreSupplierPageDto.getIndustryId() == 0) {
            List<SwcyIndustryEntity> swcyIndustryEntities = industryService.getIndustryList();
            for (SwcyIndustryEntity swcyIndustryEntity : swcyIndustryEntities) {
                industryIds.add(swcyIndustryEntity.getId());
            }
        } else {
            industryIds.add(loadMoreSupplierPageDto.getIndustryId());
        }
        LgmnPage<SwcySupplierEntity> swcySupplierEntityLgmnPage = swcySupplierAPIService.getSupplierPageByIndustryId(industryIds, loadMoreSupplierPageDto.getPageNumber(), loadMoreSupplierPageDto.getPageSize());
        LgmnPage<SupplierInfoVo> supplierInfoVoLgmnPage = new SupplierInfoVo().getVoPage(swcySupplierEntityLgmnPage, SupplierInfoVo.class);
        return Result.success(supplierInfoVoLgmnPage);
    }


    public Result getSupplierCommodityPage(GetSupplierCommodityPageDto getSupplierCommodityPageDto) throws Exception {
        List<GetSupplierCommodityPageVo> list = new ArrayList<>();
        List<SwcySupplierCategoryEntity> categoryEntities = swcySupplierCategoryAPIService.getSupplierCategoryListBySupplierId(getSupplierCommodityPageDto.getSupplierId());
        for (SwcySupplierCategoryEntity swcySupplierCategoryEntity : categoryEntities) {
            LgmnPage<SwcySupplierCommodityEntity> swcySupplierCommodityEntityLgmnPage = swcySupplierCommodityAPIService.getSupplierCommodityPageByCategoryId(swcySupplierCategoryEntity.getId(), 0, 10);
            LgmnPage<SupplierCommodityInfoVo> supplierCommodityInfoVoLgmnPage = new SupplierCommodityInfoVo().getVoPage(swcySupplierCommodityEntityLgmnPage, SupplierCommodityInfoVo.class);
            GetSupplierCommodityPageVo getSupplierCommodityPageVo = new GetSupplierCommodityPageVo().getVo(swcySupplierCategoryEntity, GetSupplierCommodityPageVo.class);
            getSupplierCommodityPageVo.setLgmnPage(supplierCommodityInfoVoLgmnPage);
            list.add(getSupplierCommodityPageVo);
        }
        return Result.success(list);
    }

    public Result loadMoreSupplierCommodityPage(LoadMoreSupplierCommodityPageDto loadMoreSupplierCommodityPageDto) throws Exception {
        LgmnPage<SwcySupplierCommodityEntity> swcySupplierCommodityEntityLgmnPage = swcySupplierCommodityAPIService.getSupplierCommodityPageByCategoryId(loadMoreSupplierCommodityPageDto.getCategoryId(), loadMoreSupplierCommodityPageDto.getPageNumber(), loadMoreSupplierCommodityPageDto.getPageSize());
        LgmnPage<SupplierCommodityInfoVo> supplierCommodityInfoVoLgmnPage = new SupplierCommodityInfoVo().getVoPage(swcySupplierCommodityEntityLgmnPage, SupplierCommodityInfoVo.class);
        return Result.success(supplierCommodityInfoVoLgmnPage);
    }

    public Result getSupplierCommodityLatestPrice(GetSupplierCommodityLatestPriceDto getSupplierCommodityLatestPriceDto) throws Exception {
        List<SwcySupplierCommodityEntity> swcySupplierCommodityEntities = swcySupplierCommodityAPIService.getSupplierCommoditysByIds(getSupplierCommodityLatestPriceDto.getIds());
        List<SupplierCommodityLatestPriceVo> supplierCommodityLatestPriceVos = new SupplierCommodityLatestPriceVo().getVoList(swcySupplierCommodityEntities, SupplierCommodityLatestPriceVo.class);
        return Result.success(supplierCommodityLatestPriceVos);
    }

    public Result getSupplierOrderPage(LgmnUserInfo lgmnUserInfo, SupplierOrderPageDto supplierOrderPageDto) throws Exception {
        LgmnPage<SwcySupplierOrderEntity> swcySupplierOrderEntityLgmnPage = swcySupplierOrderAPIService.getSupplierOrderPageByStoreOwnerId(lgmnUserInfo.getId(), supplierOrderPageDto.getPageNumber(), supplierOrderPageDto.getPageSize());
        LgmnPage<SupplierOrderPageVo> supplierOrderPageVoLgmnPage = new SupplierOrderPageVo().getVoPage(swcySupplierOrderEntityLgmnPage, SupplierOrderPageVo.class);
        for (SupplierOrderPageVo supplierOrderPageVo : supplierOrderPageVoLgmnPage.getList()) {
            SwcyReceivingAddressEntity swcyReceivingAddressEntity = swcyReceivingAddressApiService.getReceivingAddressById(supplierOrderPageVo.getAddressId());
            List<SwcySupplierOrderDetailEntity> detailList = swcySupplierOrderDetailAPIService.getSupplierOrderDetailListByOrderId(supplierOrderPageVo.getId());
            List<SupplierOrderDetailInfoVo> supplierOrderDetailInfoVos = new SupplierOrderDetailInfoVo().getVoList(detailList, SupplierOrderDetailInfoVo.class);
            ReceivingAddressVo receivingAddressVo = new ReceivingAddressVo().getVo(swcyReceivingAddressEntity, ReceivingAddressVo.class);
            SwcyStoreEntity swcyStoreEntity = sStoreService.getStoreById(supplierOrderPageVo.getStoreId());
            StoreInfoVo storeInfoVo = new StoreInfoVo().getVo(swcyStoreEntity, StoreInfoVo.class);
            SwcySupplierEntity swcySupplierEntity = swcySupplierAPIService.getSupplierById(supplierOrderPageVo.getSupplierId());
            SupplierInfoVo supplierInfoVo = new SupplierInfoVo().getVo(swcySupplierEntity, SupplierInfoVo.class);
            supplierOrderPageVo.setReceivingAddressVo(receivingAddressVo);
            supplierOrderPageVo.setStoreInfoVo(storeInfoVo);
            supplierOrderPageVo.setSupplierInfoVo(supplierInfoVo);
            supplierOrderPageVo.setSupplierOrderDetailInfoVos(supplierOrderDetailInfoVos);
        }
        return Result.success(supplierOrderPageVoLgmnPage);
    }

    public Result supplierOrderConfirmReceipt(LgmnUserInfo lgmnUserInfo, SupplierOrderConfirmReceiptDto supplierOrderConfirmReceiptDto) {
        SwcySupplierOrderEntity swcySupplierOrderEntity = swcySupplierOrderAPIService.getSupplierOrderById(supplierOrderConfirmReceiptDto.getId());
        if (!swcySupplierOrderEntity.getStoreOwnerId().equals(lgmnUserInfo.getId())) {
            return Result.error(ResultEnum.DATA_NOT_EXISTS);
        }

        if (swcySupplierOrderEntity.getStatus() != 3) {
            return Result.error(ResultEnum.NOT_SCHEDULED_ERROR);
        }
        swcySupplierOrderEntity.setStatus(4);
        swcySupplierOrderAPIService.save(swcySupplierOrderEntity);
        return Result.success("确认收货成功");
    }

    public Result applyForReturn(LgmnUserInfo lgmnUserInfo, ApplyForReturnDto applyForReturnDto) {
        SwcySupplierOrderEntity swcySupplierOrderEntity = swcySupplierOrderAPIService.getSupplierOrderById(applyForReturnDto.getId());
        if (!swcySupplierOrderEntity.getStoreOwnerId().equals(lgmnUserInfo.getId())) {
            return Result.error(ResultEnum.DATA_NOT_EXISTS);
        }

        if (swcySupplierOrderEntity.getStatus() != 3 || "".equals(applyForReturnDto.getRefundsReason())) {
            return Result.error(ResultEnum.NOT_SCHEDULED_ERROR);
        }

        swcySupplierOrderEntity.setStatus(5);
        swcySupplierOrderEntity.setRefundsReason(applyForReturnDto.getRefundsReason());
        swcySupplierOrderAPIService.save(swcySupplierOrderEntity);
        return Result.success("申请退货成功");
    }

    public Result supplierUnifiedOrderWxPay(HttpServletRequest req, SupplierUnifiedOrderDto unifiedOrderDto, LgmnUserInfo lgmnUserInfo) {
        SwcySupplierEntity swcySupplierEntity = swcySupplierAPIService.getSupplierById(unifiedOrderDto.getSupplierId());
        // 全局订单号
        String orderNo = "GYS" + System.currentTimeMillis();
        Map<String, Object> unifiedOrderMap = swcySupplierCommodityAPIService.getCommodityByUnifiedOrderDto(unifiedOrderDto);
        BigDecimal sunPrice = new BigDecimal(unifiedOrderMap.get("sunPrice").toString());

        // 统一下单
        NutMap nutMap = unifiedOrder(req, sunPrice, orderNo, "共享店商品支付", supplierNotifyurl, appid, mchid, key);
        if (!nutMap.get("return_code").equals("SUCCESS")) {
            System.err.println(nutMap.toString());
            return Result.error(ResultEnum.NOT_SCHEDULED_ERROR);
        }

        // 保存订单信息
        SwcySupplierOrderEntity swcySupplierOrderEntity = saveSupplierOrder(sunPrice, orderNo, unifiedOrderDto, lgmnUserInfo);
        // 保存订单明细
        List<SwcySupplierCommodityEntity> list = (List<SwcySupplierCommodityEntity>)unifiedOrderMap.get("list");
        saveSupplierOrderDetails(swcySupplierOrderEntity.getId(), list, unifiedOrderDto.getIdAndCount());

        // 保存流水
        orderService.getFlowEntity(lgmnUserInfo.getId(), swcySupplierOrderEntity.getId(), orderNo, swcySupplierEntity.getUid(), sunPrice);
        return Result.success(nutMap);
    }

    private SwcySupplierOrderEntity saveSupplierOrder(BigDecimal sunPrice, String orderNo, SupplierUnifiedOrderDto unifiedOrderDto, LgmnUserInfo lgmnUserInfo) {
        SwcySupplierEntity swcySupplierEntity = swcySupplierAPIService.getSupplierById(unifiedOrderDto.getSupplierId());
        SwcySupplierOrderEntity swcySupplierOrderEntity = new SwcySupplierOrderEntity();
        swcySupplierOrderEntity.setStatus(0);
        swcySupplierOrderEntity.setMoney(sunPrice);
        swcySupplierOrderEntity.setPayChannel("WxPay");
        swcySupplierOrderEntity.setPayNum(orderNo);
        swcySupplierOrderEntity.setSupplierId(unifiedOrderDto.getSupplierId());
        swcySupplierOrderEntity.setStoreOwnerId(lgmnUserInfo.getId());
        swcySupplierOrderEntity.setStoreId(unifiedOrderDto.getStoreId());
        swcySupplierOrderEntity.setAddressId(unifiedOrderDto.getAddressId());
        swcySupplierOrderEntity.setOrderTime(new Timestamp(System.currentTimeMillis()));
        return swcySupplierOrderAPIService.save(swcySupplierOrderEntity);
    }

    private List<SwcySupplierOrderDetailEntity> saveSupplierOrderDetails(String orderId, List<SwcySupplierCommodityEntity> swcySupplierCommodityEntities, Map<Integer, Integer> commodityIdAndNum) {
        List<SwcySupplierOrderDetailEntity> list = new ArrayList<>();
        for (SwcySupplierCommodityEntity swcySupplierCommodityEntity : swcySupplierCommodityEntities) {
            SwcySupplierCategoryEntity swcySupplierCategoryEntity = swcySupplierCategoryAPIService.getSupplierCategoryById(swcySupplierCommodityEntity.getCategoryId());
            SwcySupplierOrderDetailEntity swcySupplierOrderDetailEntity = new SwcySupplierOrderDetailEntity();
            swcySupplierOrderDetailEntity.setCommodityId(swcySupplierCommodityEntity.getId());
            swcySupplierOrderDetailEntity.setOrderId(orderId);
            swcySupplierOrderDetailEntity.setCommodityName(swcySupplierCommodityEntity.getName());
            swcySupplierOrderDetailEntity.setCommodityType(swcySupplierCategoryEntity.getName());
            swcySupplierOrderDetailEntity.setCover(swcySupplierCommodityEntity.getCover());
            swcySupplierOrderDetailEntity.setPrice(swcySupplierCommodityEntity.getRetailPrice());
            swcySupplierOrderDetailEntity.setNum(commodityIdAndNum.get(swcySupplierCommodityEntity.getId()));
            list.add(swcySupplierOrderDetailEntity);
        }
        return swcySupplierOrderDetailAPIService.saveSupplierOrderDetails(list);
    }

    public void supplierWxPayCallBack(HttpServletRequest req, HttpServletResponse response) throws Exception {
        System.out.println("\n\n\n\n\n>>>>>>>>>>>>>>>>>>>>>>>>微信回调开始>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n\n\n\n");
        String xmlStr = getXmlString(req);

        Map<String, Object> map = xmlToMap(xmlStr);
//        // 验证返回是否成功
        if (map.get("return_code").equals("SUCCESS")) {
            System.out.println("通信成功>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            // 验证签名是否正确
            if (WxPaySign.createSign(key, map).equals(map.get("sign"))) {
                // 修改订单支付状态
                System.out.println(map.get("out_trade_no").toString());
                SwcyFlowEntity swcyFlowEntity = swcyFlowApiService.getFlowByPayNo(map.get("out_trade_no").toString());
                if (swcyFlowEntity.getResult() == 0) {
                    SwcySupplierOrderEntity swcySupplierOrderEntity = swcySupplierOrderAPIService.getSupplierOrderById(swcyFlowEntity.getOrderId());
                    swcySupplierOrderEntity.setStatus(2);
                    swcySupplierOrderEntity.setPayTime(new Timestamp(System.currentTimeMillis()));
                    swcySupplierOrderAPIService.save(swcySupplierOrderEntity);
                    swcyFlowEntity.setResult(1);
                    swcyFlowApiService.save(swcyFlowEntity);

                    // 更新供应商资产
                    SwcySupplierEntity swcySupplierEntity = swcySupplierAPIService.getSupplierById(swcySupplierOrderEntity.getSupplierId());
                    SwcyAssetsEntity swcyAssetsEntity = assetsService.getAsstesByUid(swcySupplierEntity.getUid());
                    if (swcyAssetsEntity == null) {
                        SwcyAssetsEntity newAssets = new SwcyAssetsEntity();
                        newAssets.setUid(swcySupplierEntity.getUid());
                        newAssets.setAssets(swcySupplierOrderEntity.getMoney());
                        newAssets.setMoney(new BigDecimal(0));
                        newAssets.setCommission(new BigDecimal(0));
                        newAssets.setFinance(new BigDecimal(0));
                        newAssets.setCreditLine(new BigDecimal(0));
                        assetsService.save(newAssets);
                    } else {
                        swcyAssetsEntity.setAssets(swcyAssetsEntity.getAssets().add(swcySupplierOrderEntity.getMoney()));
                        assetsService.save(swcyAssetsEntity);
                    }

                }
            }
        }
        String str = returnXML(map.get("return_code").toString());
        response.getWriter().write(str);
        response.getWriter().flush();
        System.out.println("\n\n\n\n\n>>>>>>>>>>>>>>>>>>>>>>>>微信回调结束>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n\n\n\n");
    }

    public Result leagueStoreGetSupplier(LeagueStoreGetSupplierDto leagueStoreGetSupplierDto) throws Exception {
        List<Integer> industryIds = new ArrayList<>();
        industryIds.add(leagueStoreGetSupplierDto.getIndustryId());
        List<SwcySupplierEntity> list = swcySupplierAPIService.getSupplierListByIndustryId(industryIds);
        if (list.size() > 0) {
            SwcySupplierEntity swcySupplierEntity = list.get(0);
            SupplierInfoVo supplierInfoVo = new SupplierInfoVo();
            ObjectTransfer.transValue(swcySupplierEntity, supplierInfoVo);
            return Result.success(supplierInfoVo);
        } else {
            return Result.serverError("暂无供应商可用");
        }
    }

    public Result getLeagueStoreOrderPage(GetLeagueStoreOrderPageDto getLeagueStoreOrderPageDto) throws Exception {
        LgmnPage<SwcySupplierOrderEntity> lgmnPage = swcySupplierOrderAPIService.getSupplierOrderPageByStoreId(getLeagueStoreOrderPageDto.getStoreId(), getLeagueStoreOrderPageDto.getPageNumber(), getLeagueStoreOrderPageDto.getPageSize());
        LgmnPage<LeagueStoreOrderPageVo> leagueStoreOrderPageVoLgmnPage = new LgmnPage<>();
        ObjectTransfer.transValue(lgmnPage, leagueStoreOrderPageVoLgmnPage);
        List<LeagueStoreOrderPageVo> leagueStoreOrderPageVos = new ArrayList<>();
        for (SwcySupplierOrderEntity swcySupplierOrderEntity : lgmnPage.getList()) {
            LeagueStoreOrderPageVo leagueStoreOrderPageVo = new LeagueStoreOrderPageVo();
            ObjectTransfer.transValue(swcySupplierOrderEntity, leagueStoreOrderPageVo);

            List<SwcySupplierOrderDetailEntity> swcySupplierOrderDetailEntities = swcySupplierOrderDetailAPIService.getSupplierOrderDetailListByOrderId(swcySupplierOrderEntity.getId());
            leagueStoreOrderPageVo.setList(swcySupplierOrderDetailEntities);
            leagueStoreOrderPageVo.setCommTypeSum(swcySupplierOrderDetailEntities.size());
            leagueStoreOrderPageVos.add(leagueStoreOrderPageVo);
        }
        leagueStoreOrderPageVoLgmnPage.setList(leagueStoreOrderPageVos);
        return Result.success(leagueStoreOrderPageVoLgmnPage);
    }

    public Result leagueStoreOrderConfirmReceipt(SupplierOrderConfirmReceiptDto supplierOrderConfirmReceiptDto) throws Exception {
        SwcySupplierOrderEntity swcySupplierOrderEntity = swcySupplierOrderAPIService.getSupplierOrderById(supplierOrderConfirmReceiptDto.getId());
        swcySupplierOrderEntity.setStatus(4);
        swcySupplierOrderAPIService.save(swcySupplierOrderEntity);
        SwcyStoreEntity swcyStoreEntity = sStoreService.getStoreById(swcySupplierOrderEntity.getStoreId());

        List<SwcySupplierOrderDetailEntity> swcySupplierOrderDetailEntities = swcySupplierOrderDetailAPIService.getSupplierOrderDetailListByOrderId(swcySupplierOrderEntity.getId());
        for (SwcySupplierOrderDetailEntity swcySupplierOrderDetailEntity : swcySupplierOrderDetailEntities) {
            SwcySupplierCommodityEntity swcySupplierCommodityEntity = swcySupplierCommodityAPIService.getSupplierCommodityById(swcySupplierOrderDetailEntity.getCommodityId());
            List<SwcyCommodityTypeEntity> swcyCommodityTypeEntityList = swcyCommodityTypeApiService.getCommodityTypeByStoreIdAndSupplierCategoryId(swcySupplierOrderEntity.getStoreId(), swcySupplierCommodityEntity.getCategoryId());
            if (swcyCommodityTypeEntityList.size() > 0) {
                SwcyCommodityTypeEntity swcyCommodityTypeEntity = swcyCommodityTypeEntityList.get(0);
                List<SwcyCommodityEntity> swcyCommodityEntities = swcyCommodityApiService.getCommodityBySupplierCommodityIdAndTypeId(swcySupplierCommodityEntity.getId(), swcyCommodityTypeEntity.getId());
                if (swcyCommodityEntities.size() > 0) {
                    SwcyCommodityEntity swcyCommodityEntity = swcyCommodityEntities.get(0);
                    swcyCommodityEntity.setName(swcySupplierCommodityEntity.getName());
                    swcyCommodityEntity.setCover(swcySupplierCommodityEntity.getCover());
                    swcyCommodityEntity.setDetail(swcySupplierCommodityEntity.getDetail());
                    swcyCommodityEntity.setPrice(swcySupplierOrderDetailEntity.getPrice());
                    swcyCommodityEntity.setSpecs(swcySupplierCommodityEntity.getSpecs());
                    swcyCommodityEntity.setStatus(swcySupplierCommodityEntity.getStatus());
                    swcyCommodityEntity.setStock(swcyCommodityEntity.getStock() + swcySupplierOrderDetailEntity.getNum());
                    swcyCommodityApiService.saveCommodity(swcyCommodityEntity);
                } else {
                    SwcyCommodityEntity swcyCommodityEntity = getCommodity(swcyCommodityTypeEntity.getId(), swcySupplierCommodityEntity, swcyStoreEntity.getStarCode(), swcySupplierOrderDetailEntity.getNum());
                    swcyCommodityApiService.saveCommodity(swcyCommodityEntity);
                }
            } else {
                SwcySupplierCategoryEntity swcySupplierCategoryEntity = swcySupplierCategoryAPIService.getSupplierCategoryById(swcySupplierCommodityEntity.getCategoryId());
                SwcyCommodityTypeEntity swcyCommodityTypeEntity = new SwcyCommodityTypeEntity();
                swcyCommodityTypeEntity.setStoreId(swcySupplierOrderEntity.getStoreId());
                swcyCommodityTypeEntity.setName(swcySupplierCategoryEntity.getName());
                swcyCommodityTypeEntity.setStatus(1);
                swcyCommodityTypeEntity.setSupplierCategoryId(swcySupplierCommodityEntity.getCategoryId());
                SwcyCommodityTypeEntity newCommodityType = swcyCommodityTypeApiService.save(swcyCommodityTypeEntity);

                SwcyCommodityEntity swcyCommodityEntity = getCommodity(newCommodityType.getId(), swcySupplierCommodityEntity, swcyStoreEntity.getStarCode(), swcySupplierOrderDetailEntity.getNum());
                swcyCommodityApiService.saveCommodity(swcyCommodityEntity);
            }
        }
//        Set<Integer> mapKey = leagueStoreAddCommodityDto.getMap().keySet();
//        List<Integer> supplierCommodityIds = new ArrayList<>();
//        for (Integer key : mapKey) {
//            supplierCommodityIds.add(key);
//        }
//        List<SwcySupplierCommodityEntity> swcySupplierCommodityEntitys = swcySupplierCommodityAPIService.getSupplierCommoditysByIds(supplierCommodityIds);
//        for (SwcySupplierCommodityEntity swcySupplierCommodityEntity : swcySupplierCommodityEntitys) {
//            List<SwcyCommodityTypeEntity> swcyCommodityTypeEntityList = swcyCommodityTypeApiService.getCommodityTypeByStoreIdAndSupplierCategoryId(leagueStoreAddCommodityDto.getStoreId(), swcySupplierCommodityEntity.getCategoryId());
//            if (swcyCommodityTypeEntityList.size() > 0) {
//                SwcyCommodityTypeEntity swcyCommodityTypeEntity = swcyCommodityTypeEntityList.get(0);
//                List<SwcyCommodityEntity> swcyCommodityEntities = swcyCommodityApiService.getCommodityBySupplierCommodityIdAndTypeId(swcySupplierCommodityEntity.getId(), swcyCommodityTypeEntity.getId());
//                if (swcyCommodityEntities.size() > 0) {
//                    SwcyCommodityEntity swcyCommodityEntity = swcyCommodityEntities.get(0);
//                    swcyCommodityEntity.setName(swcySupplierCommodityEntity.getName());
//                    swcyCommodityEntity.setCover(swcySupplierCommodityEntity.getCover());
//                    swcyCommodityEntity.setDetail(swcySupplierCommodityEntity.getDetail());
//                    swcyCommodityEntity.setPrice(swcySupplierCommodityEntity.getRetailPrice());
//                    swcyCommodityEntity.setSpecs(swcySupplierCommodityEntity.getSpecs());
//                    swcyCommodityEntity.setStatus(swcySupplierCommodityEntity.getStatus());
//                    swcyCommodityEntity.setStock(swcyCommodityEntity.getStock() + leagueStoreAddCommodityDto.getMap().get(swcySupplierCommodityEntity.getId()));
//                    swcyCommodityApiService.saveCommodity(swcyCommodityEntity);
//                } else {
//                    SwcyCommodityEntity swcyCommodityEntity = getCommodity(swcyCommodityTypeEntity.getId(), swcySupplierCommodityEntity, swcyStoreEntity.getStarCode(), leagueStoreAddCommodityDto.getMap().get(swcySupplierCommodityEntity.getId()), swcySupplierCommodityEntity.getId());
//                    swcyCommodityApiService.saveCommodity(swcyCommodityEntity);
//                }
//            } else {
//                SwcySupplierCategoryEntity swcySupplierCategoryEntity = swcySupplierCategoryAPIService.getSupplierCategoryById(swcySupplierCommodityEntity.getCategoryId());
//                SwcyCommodityTypeEntity swcyCommodityTypeEntity = new SwcyCommodityTypeEntity();
//                swcyCommodityTypeEntity.setStoreId(leagueStoreAddCommodityDto.getStoreId());
//                swcyCommodityTypeEntity.setName(swcySupplierCategoryEntity.getName());
//                swcyCommodityTypeEntity.setStatus(1);
//                swcyCommodityTypeEntity.setSupplierCategoryId(swcySupplierCommodityEntity.getCategoryId());
//                SwcyCommodityTypeEntity newCommodityType = swcyCommodityTypeApiService.save(swcyCommodityTypeEntity);
//
//                SwcyCommodityEntity swcyCommodityEntity = getCommodity(newCommodityType.getId(), swcySupplierCommodityEntity, swcyStoreEntity.getStarCode(), leagueStoreAddCommodityDto.getMap().get(swcySupplierCommodityEntity.getId()), swcySupplierCommodityEntity.getId());
//                swcyCommodityApiService.saveCommodity(swcyCommodityEntity);
//            }
//        }
        return Result.success("确认收货，商品同步成功");
    }

    public SwcyCommodityEntity getCommodity(Integer typeId, SwcySupplierCommodityEntity swcySupplierCommodityEntity, Integer starCode, Integer stock) {
        SwcyCommodityEntity swcyCommodityEntity = new SwcyCommodityEntity();
        BeanUtils.copyProperties(swcySupplierCommodityEntity,swcyCommodityEntity, "id");
        swcyCommodityEntity.setTypeId(typeId);
        swcyCommodityEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        swcyCommodityEntity.setDelFlag(0);
        swcyCommodityEntity.setStarCode(starCode);
        swcyCommodityEntity.setStock(stock);
        swcyCommodityEntity.setPrice(swcySupplierCommodityEntity.getRetailPrice());
        swcyCommodityEntity.setSupplierCommodityId(swcySupplierCommodityEntity.getId());
        swcyCommodityEntity.setStatus(swcySupplierCommodityEntity.getStatus());
        swcyCommodityEntity.setNotes(swcySupplierCommodityEntity.getNotes());
        return swcyCommodityEntity;
    }

    public Result getLeagueStoreOrderDetails(SupplierOrderConfirmReceiptDto supplierOrderConfirmReceiptDto) throws Exception {
        SwcySupplierOrderEntity swcySupplierOrderEntity = swcySupplierOrderAPIService.getSupplierOrderById(supplierOrderConfirmReceiptDto.getId());
        List<SwcySupplierOrderDetailEntity> swcySupplierOrderDetailEntities = swcySupplierOrderDetailAPIService.getSupplierOrderDetailListByOrderId(swcySupplierOrderEntity.getId());
        GetLeagueStoreOrderDetailsVo getLeagueStoreOrderDetailsVo = new GetLeagueStoreOrderDetailsVo();
        getLeagueStoreOrderDetailsVo.setSwcySupplierOrderEntity(swcySupplierOrderEntity);
        getLeagueStoreOrderDetailsVo.setSwcySupplierOrderDetailEntities(swcySupplierOrderDetailEntities);
        return Result.success(getLeagueStoreOrderDetailsVo);
    }
}
