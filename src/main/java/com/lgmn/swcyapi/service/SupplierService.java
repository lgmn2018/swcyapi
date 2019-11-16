package com.lgmn.swcyapi.service;

import com.lgmn.common.domain.LgmnPage;
import com.lgmn.common.domain.LgmnUserInfo;
import com.lgmn.common.result.Result;
import com.lgmn.common.result.ResultEnum;
import com.lgmn.swcy.basic.entity.*;
import com.lgmn.swcyapi.dto.supplier.*;
import com.lgmn.swcyapi.service.address.SwcyReceivingAddressApiService;
import com.lgmn.swcyapi.service.flow.SwcyFlowApiService;
import com.lgmn.swcyapi.service.industry.IndustryService;
import com.lgmn.swcyapi.service.store.SStoreService;
import com.lgmn.swcyapi.service.supplier.*;
import com.lgmn.swcyapi.vo.order.ReceivingAddressVo;
import com.lgmn.swcyapi.vo.supplier.*;
import org.nutz.lang.util.NutMap;
import org.nutz.weixin.util.WxPaySign;
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
        List<GetSupplierPageVo> list = new ArrayList<>();
        for (SwcyIndustryEntity swcyIndustryEntity : swcyIndustryEntities) {
            GetSupplierPageVo getSupplierPageVo = new GetSupplierPageVo().getVo(swcyIndustryEntity, GetSupplierPageVo.class);
            LgmnPage<SwcySupplierEntity> swcySupplierEntityLgmnPage = swcySupplierAPIService.getSupplierPageByIndustryId(swcyIndustryEntity.getId(), 0, 10);
            LgmnPage<SupplierInfoVo> supplierInfoVoLgmnPage = new SupplierInfoVo().getVoPage(swcySupplierEntityLgmnPage, SupplierInfoVo.class);
            getSupplierPageVo.setInfoVoLgmnPage(supplierInfoVoLgmnPage);
            list.add(getSupplierPageVo);
        }
        return Result.success(list);
    }

    public Result loadMoreSupplierPage(LoadMoreSupplierPageDto loadMoreSupplierPageDto) throws Exception {
        LgmnPage<SwcySupplierEntity> swcySupplierEntityLgmnPage = swcySupplierAPIService.getSupplierPageByIndustryId(loadMoreSupplierPageDto.getIndustryId(), loadMoreSupplierPageDto.getPageNumber(), loadMoreSupplierPageDto.getPageSize());
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
        SwcySupplierOrderEntity swcySupplierOrderEntity = new SwcySupplierOrderEntity();
        swcySupplierOrderEntity.setStatus(0);
        swcySupplierOrderEntity.setMoney(sunPrice);
        swcySupplierOrderEntity.setPayChannel("WxPay");
        swcySupplierOrderEntity.setPayNum(orderNo);
        swcySupplierOrderEntity.setSupplierId(unifiedOrderDto.getSupplierId());
        swcySupplierOrderEntity.setStoreOwnerId(lgmnUserInfo.getId());
        swcySupplierOrderEntity.setStoreId(unifiedOrderDto.getStoreId());
        swcySupplierOrderEntity.setAddressId(unifiedOrderDto.getAddressId());
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
                SwcySupplierOrderEntity swcySupplierOrderEntity = swcySupplierOrderAPIService.getSupplierOrderById(swcyFlowEntity.getOrderId());
                swcySupplierOrderEntity.setStatus(2);
                swcySupplierOrderEntity.setPayTime(new Timestamp(System.currentTimeMillis()));
                swcySupplierOrderAPIService.save(swcySupplierOrderEntity);
                swcyFlowEntity.setResult(1);
                swcyFlowApiService.save(swcyFlowEntity);
            }
        }
        String str = returnXML(map.get("return_code").toString());
        response.getWriter().write(str);
        response.getWriter().flush();
        System.out.println("\n\n\n\n\n>>>>>>>>>>>>>>>>>>>>>>>>微信回调结束>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n\n\n\n");
    }
}
