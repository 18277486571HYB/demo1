package com.hyb.demo1.serviceImpl;

import com.hyb.demo1.entity.Friend;
import com.hyb.demo1.mapper.FriendMapper;
import com.hyb.demo1.service.FriendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyb
 * @since 2022-06-16 15:40:59
 */
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {

}
