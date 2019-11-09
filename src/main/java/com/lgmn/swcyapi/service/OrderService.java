package com.lgmn.swcyapi.service;

import com.lgmn.common.domain.LgmnPage;
import com.lgmn.common.domain.LgmnUserInfo;
import com.lgmn.common.result.Result;
import com.lgmn.common.result.ResultEnum;
import com.lgmn.common.utils.ObjectTransfer;
import com.lgmn.swcy.basic.entity.*;
import com.lgmn.swcy.basic.service.SwcyReceivingAddressService;
import com.lgmn.swcyapi.dto.order.GetOrderPageByStoreIdDto;
import com.lgmn.swcyapi.dto.order.OrderDetailDto;
import com.lgmn.swcyapi.dto.order.OrderPageDto;
import com.lgmn.swcyapi.dto.order.UnifiedOrderDto;
import com.lgmn.swcyapi.service.address.SwcyReceivingAddressApiService;
import com.lgmn.swcyapi.service.commodity.SwcyCommodityApiService;
import com.lgmn.swcyapi.service.commodity.SwcyCommodityTypeApiService;
import com.lgmn.swcyapi.service.flow.SwcyFlowApiService;
import com.lgmn.swcyapi.service.order.SOrderDetailService;
import com.lgmn.swcyapi.service.order.SOrderService;
import com.lgmn.swcyapi.service.store.SStoreService;
import com.lgmn.swcyapi.service.user.UserService;
import com.lgmn.swcyapi.vo.order.GetOrderPageByStoreIdVo;
import com.lgmn.swcyapi.vo.order.OrderDetailListVo;
import com.lgmn.swcyapi.vo.order.OrderDetailVo;
import com.lgmn.swcyapi.vo.order.OrderPageVo;
import com.lgmn.userservices.basic.entity.LgmnUserEntity;
import com.lgmn.userservices.basic.service.LgmnUserService;
import org.nutz.lang.Lang;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.weixin.bean.WxPayUnifiedOrder;
import org.nutz.weixin.impl.WxApi2Impl;
import org.nutz.weixin.util.WxPaySign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderService {

    @Autowired
    SOrderService sOrderService;

    @Autowired
    SStoreService sStoreService;

    @Autowired
    SOrderDetailService sOrderDetailService;

    @Autowired
    SwcyCommodityApiService swcyCommodityApiService;

    @Autowired
    SwcyCommodityTypeApiService swcyCommodityTypeApiService;

    @Autowired
    SwcyFlowApiService swcyFlowApiService;

    @Autowired
    UserService userService;

    @Autowired
    SwcyReceivingAddressApiService swcyReceivingAddressApiService;


    @Value("${wxparame.appid}")
    private String appid;

    @Value("${wxparame.mchid}")
    private String mchid;

    @Value("${wxparame.key}")
    private String key;

    @Value("${wxparame.notifyurl}")
    private String notifyurl;

    public Result getOrderPage (OrderPageDto orderPageDto, String uid) throws Exception {
        LgmnPage<SwcyOrderEntity> lgmnPage = sOrderService.getOrderPage(orderPageDto, uid);
        LgmnPage<OrderPageVo> orderPageVoLgmnPage = new OrderPageVo().getVoPage(lgmnPage, OrderPageVo.class);
        for (OrderPageVo orderPageVo : orderPageVoLgmnPage.getList()) {
            SwcyStoreEntity swcyStoreEntity = sStoreService.getStoreById(orderPageVo.getStoreId());
            List<SwcyOrderDetailEntity> orderDetailEntities = sOrderDetailService.getOrderDetailsByOrderId(orderPageVo.getId());
            SwcyReceivingAddressEntity swcyReceivingAddressEntity = swcyReceivingAddressApiService.getReceivingAddressById(orderPageVo.getAddressId());
            orderPageVo.setStoreName(swcyStoreEntity.getStoreName());
            orderPageVo.setImageUrl(orderDetailEntities.get(0).getCover());
            orderPageVo.setAddress(swcyReceivingAddressEntity.getProvinceName() + swcyReceivingAddressEntity.getCityName() + swcyReceivingAddressEntity.getAreaName() + swcyReceivingAddressEntity.getAddress());
        }
        return Result.success(orderPageVoLgmnPage);
    }

    public Result getOrderDetailById (OrderDetailDto orderDetailDto) throws Exception {
        SwcyOrderEntity swcyOrderEntity = sOrderService.getOrderById(orderDetailDto.getOrderId());
        OrderPageVo orderPageVo = new OrderPageVo().getVo(swcyOrderEntity, OrderPageVo.class);
        List<SwcyOrderDetailEntity> swcyOrderDetailEntities = sOrderDetailService.getOrderDetailsByOrderId(swcyOrderEntity.getId());
        List<OrderDetailListVo> orderDetailListVos = new OrderDetailListVo().getVoList(swcyOrderDetailEntities, OrderDetailListVo.class);
        SwcyStoreEntity swcyStoreEntity = sStoreService.getStoreById(swcyOrderEntity.getStoreId());
        SwcyReceivingAddressEntity swcyReceivingAddressEntity = swcyReceivingAddressApiService.getReceivingAddressById(swcyOrderEntity.getAddressId());
        orderPageVo.setAddress(swcyReceivingAddressEntity.getProvinceName() + swcyReceivingAddressEntity.getCityName() + swcyReceivingAddressEntity.getAreaName() + swcyReceivingAddressEntity.getAddress());
        orderPageVo.setImageUrl(swcyStoreEntity.getPhoto());
        orderPageVo.setStoreName(swcyStoreEntity.getStoreName());
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrderPageVo(orderPageVo);
        orderDetailVo.setOrderDetailListVo(orderDetailListVos);
        return Result.success(orderDetailVo);
    }

    public Result wxPay(HttpServletRequest req, UnifiedOrderDto unifiedOrderDto, LgmnUserInfo lgmnUserInfo) {
        SwcyStoreEntity swcyStoreEntity = sStoreService.getStoreById(unifiedOrderDto.getStoreId());
        Map<String, Object> unifiedOrderMap = swcyCommodityApiService.getCommodityByUnifiedOrderDto(unifiedOrderDto);
        BigDecimal sunPrice = new BigDecimal(unifiedOrderMap.get("sunPrice").toString());
        // 全局订单号
        String orderNo = "SPGM" + System.currentTimeMillis();
        // 统一下单
        NutMap nutMap = unifiedOrder(req, sunPrice, orderNo);
        if (!nutMap.get("return_code").equals("SUCCESS")) {
            System.err.println(nutMap.toString());
            return Result.error(ResultEnum.NOT_SCHEDULED_ERROR);
        }

        // 添加订单
        SwcyOrderEntity newOrder = getOrder(sunPrice, orderNo, lgmnUserInfo.getId(), unifiedOrderDto.getStoreId(), unifiedOrderDto.getAddressId(), swcyStoreEntity.getUid());

        // 添加订单明细
        List<SwcyCommodityEntity> list = (List<SwcyCommodityEntity>)unifiedOrderMap.get("list");
        getOrderDetails(list, newOrder.getId(), unifiedOrderDto.getIdAndCount());

        // 流水
        getFlowEntity(lgmnUserInfo.getId(), newOrder.getId(), orderNo, swcyStoreEntity.getUid(), sunPrice);
        return Result.success(nutMap);
    }

    public Result confirmReceipt(OrderDetailDto orderDetailDto, LgmnUserInfo lgmnUserInfo) {
        SwcyOrderEntity swcyOrderEntity = sOrderService.getOrderById(orderDetailDto.getOrderId());
        if (swcyOrderEntity.getStatus() != 3 || !lgmnUserInfo.getId().equals(swcyOrderEntity.getUid())) {
            return Result.error(ResultEnum.NOT_SCHEDULED_ERROR);
        }
        swcyOrderEntity.setStatus(4);
        sOrderService.save(swcyOrderEntity);
        return Result.success("确认收货成功");
    }

    public Result getOrderPageByStoreId(LgmnUserInfo lgmnUserInfo, GetOrderPageByStoreIdDto getOrderPageByStoreIdDto) throws Exception {
        SwcyStoreEntity swcyStoreEntity = sStoreService.getStoreById(getOrderPageByStoreIdDto.getStoreId());
        if (!swcyStoreEntity.getUid().equals(lgmnUserInfo.getId())) {
            return Result.error(ResultEnum.DATA_NOT_EXISTS);
        }
        LgmnPage<SwcyOrderEntity> swcyOrderEntityLgmnPage = sOrderService.getOrderPageByStoreId(getOrderPageByStoreIdDto);
        LgmnPage<GetOrderPageByStoreIdVo> getOrderPageByStoreIdVoLgmnPage = new LgmnPage<>();
        ObjectTransfer.transValue(swcyOrderEntityLgmnPage, getOrderPageByStoreIdVoLgmnPage);
        List<GetOrderPageByStoreIdVo> getOrderPageByStoreIdVos = new ArrayList<>();
        for(SwcyOrderEntity swcyOrderEntity : swcyOrderEntityLgmnPage.getList()) {
            GetOrderPageByStoreIdVo getOrderPageByStoreIdVo = new GetOrderPageByStoreIdVo();
            LgmnUserEntity lgmnUserEntity = userService.getUserById(swcyOrderEntity.getUid());
            SwcyReceivingAddressEntity swcyReceivingAddressEntity = swcyReceivingAddressApiService.getReceivingAddressById(swcyOrderEntity.getAddressId());
            ObjectTransfer.transValue(swcyOrderEntity, getOrderPageByStoreIdVo);
            ObjectTransfer.transValue(lgmnUserEntity, getOrderPageByStoreIdVo);
            ObjectTransfer.transValue(swcyReceivingAddressEntity, getOrderPageByStoreIdVo);
            getOrderPageByStoreIdVos.add(getOrderPageByStoreIdVo);
        }
        getOrderPageByStoreIdVoLgmnPage.getList().clear();
        getOrderPageByStoreIdVoLgmnPage.setList(getOrderPageByStoreIdVos);
        return Result.success(getOrderPageByStoreIdVoLgmnPage);
    }

    /**
     * 统一下单
     * @param req
     * @param sunPrice
     * @return
     */
    private NutMap unifiedOrder(HttpServletRequest req, BigDecimal sunPrice, String orderNo) {
        WxPayUnifiedOrder wxPayUnifiedOrder = new WxPayUnifiedOrder();
        wxPayUnifiedOrder.setAppid(appid);                                       // APPID
        wxPayUnifiedOrder.setMch_id(mchid);                                      // 商户号ID
        wxPayUnifiedOrder.setNonce_str(R.UU32());                                // 随机字符串
        wxPayUnifiedOrder.setBody("商品购买");                                    // 描述
        wxPayUnifiedOrder.setOut_trade_no(orderNo);                              // 订单号
        wxPayUnifiedOrder.setTotal_fee(1);                                       // 金额
//        wxPayUnifiedOrder.setTotal_fee(sunPrice.multiply(new BigDecimal(100)).intValue());
        wxPayUnifiedOrder.setSpbill_create_ip(Lang.getIP(req));                  // 终端IP
        wxPayUnifiedOrder.setNotify_url(notifyurl);                              // 回调URL
        wxPayUnifiedOrder.setTrade_type("APP");                                  // 交易类型
        WxApi2Impl wxApi2Impl = new WxApi2Impl();
        NutMap nutMap = wxApi2Impl.pay_unifiedorder(key, wxPayUnifiedOrder);
        return nutMap;
    }

    /**
     * 保存订单
     * @param sunPrice
     * @param payNo
     * @param uid
     * @param storeId
     * @param addressId
     * @param storeUid
     * @return
     */
    private SwcyOrderEntity getOrder(BigDecimal sunPrice, String payNo, String uid, Integer storeId, Integer addressId, String storeUid) {
        SwcyOrderEntity swcyOrderEntity = new SwcyOrderEntity();
        swcyOrderEntity.setStatus(0);
        swcyOrderEntity.setMoney(sunPrice);
        swcyOrderEntity.setOrderTime(new Timestamp(System.currentTimeMillis()));
        swcyOrderEntity.setPayChannel("WxPay");
        swcyOrderEntity.setPayNum(payNo);
        swcyOrderEntity.setUid(uid);
        swcyOrderEntity.setStoreId(storeId);
        swcyOrderEntity.setAddressId(addressId);
        swcyOrderEntity.setStoreOwnerId(storeUid);
        SwcyOrderEntity newOrder = sOrderService.save(swcyOrderEntity);
        return newOrder;
    }

    /**
     * 保存订单明细
     * @param list
     * @param orderId
     * @param idAndCount
     * @return
     */
    private List<SwcyOrderDetailEntity> getOrderDetails(List<SwcyCommodityEntity> list, String orderId, Map<Integer, Integer> idAndCount) {
        List<SwcyOrderDetailEntity> swcyOrderDetailEntities = new ArrayList<>();
        for(SwcyCommodityEntity swcyCommodityEntity : list) {
            SwcyCommodityTypeEntity swcyCommodityTypeEntity = swcyCommodityTypeApiService.getCommodityTypeById(swcyCommodityEntity.getTypeId());
            SwcyOrderDetailEntity swcyOrderDetailEntity = new SwcyOrderDetailEntity();
            swcyOrderDetailEntity.setOrderId(orderId);
            swcyOrderDetailEntity.setCommodityId(swcyCommodityEntity.getId());
            swcyOrderDetailEntity.setCommodityName(swcyCommodityEntity.getName());
            swcyOrderDetailEntity.setCommodityType(swcyCommodityTypeEntity.getName());
            swcyOrderDetailEntity.setCover(swcyCommodityEntity.getCover());
            swcyOrderDetailEntity.setPrice(swcyCommodityEntity.getPrice());
            swcyOrderDetailEntity.setNum(idAndCount.get(swcyCommodityEntity.getId()));
            swcyOrderDetailEntities.add(swcyOrderDetailEntity);
        }
        return sOrderDetailService.saveOrderDetails(swcyOrderDetailEntities);
    }

    /**
     * 保存流水
     * @param uid
     * @param orderId
     * @param payNo
     * @param storeUid
     * @param sunPrice
     * @return
     */
    private SwcyFlowEntity getFlowEntity (String uid, String orderId, String payNo, String storeUid, BigDecimal sunPrice) {
        // 流水
        SwcyFlowEntity swcyFlowEntity = new SwcyFlowEntity();
        swcyFlowEntity.setUid(uid);
        swcyFlowEntity.setOrderId(orderId);
        swcyFlowEntity.setPayNo(payNo);
        swcyFlowEntity.setMoney(sunPrice);
        swcyFlowEntity.setTypeCode("0001");
        swcyFlowEntity.setResult(0);
        swcyFlowEntity.setPayeeId(storeUid);
        swcyFlowEntity.setPayeeMoney(new BigDecimal(0));
        return swcyFlowApiService.save(swcyFlowEntity);
    }

    public void wxPayCallBack(HttpServletRequest req, HttpServletResponse response) throws Exception {
        System.out.println("\n\n\n\n\n>>>>>>>>>>>>>>>>>>>>>>>>微信回调开始>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n\n\n\n");
        String xmlStr = getXmlString(req);
        Map<String, Object> map = xmlToMap(xmlStr);
        // 验证返回是否成功
        if (map.get("return_code").equals("SUCCESS")) {
            System.out.println("通信成功>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            // 验证签名是否正确
            if (WxPaySign.createSign(key, map).equals(map.get("sign"))) {
                // 修改订单支付状态
                System.out.println(map.get("out_trade_no").toString());

                SwcyFlowEntity swcyFlowEntity = swcyFlowApiService.getFlowByPayNo(map.get("out_trade_no").toString());
                SwcyOrderEntity swcyOrderEntity = sOrderService.getOrderById(swcyFlowEntity.getOrderId());
                swcyFlowEntity.setResult(1);
                swcyFlowApiService.save(swcyFlowEntity);
                swcyOrderEntity.setStatus(2);
                sOrderService.save(swcyOrderEntity);
            }
        }
        String str = returnXML(map.get("return_code").toString());
        response.getWriter().write(str);
        response.getWriter().flush();
        System.out.println("\n\n\n\n\n>>>>>>>>>>>>>>>>>>>>>>>>微信回调结束>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n\n\n\n");
    }

    /**
     * IO解析获取微信的数据
     *
     * @param request
     * @return
     */
    private static String getXmlString(HttpServletRequest request) {
        BufferedReader reader = null;
        String line = "";
        String xmlString = null;
        try {
            reader = request.getReader();
            StringBuffer inputString = new StringBuffer();

            while ((line = reader.readLine()) != null) {
                inputString.append(line);
            }
            xmlString = inputString.toString();
        } catch (Exception e) {

        }

        return xmlString;
    }

    /**
     * XML格式字符串转换为Map
     *
     * @param strXML
     *            XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, Object> xmlToMap(String strXML) throws Exception {
        Map<String, Object> data = new HashMap<String, Object>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
        org.w3c.dom.Document doc = documentBuilder.parse(stream);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getDocumentElement().getChildNodes();
        for (int idx = 0; idx < nodeList.getLength(); ++idx) {
            Node node = nodeList.item(idx);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                data.put(element.getNodeName(), element.getTextContent());
            }
        }
        try {
            stream.close();
        } catch (Exception ex) {

        }
        return data;
    }

    /**
     * 返回给微信服务端的xml
     * @param return_code
     * @return
     */
    private static String returnXML(String return_code) {

        return "<xml><return_code><![CDATA["

                + return_code

                + "]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }
}
