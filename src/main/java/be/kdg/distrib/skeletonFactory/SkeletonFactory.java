package be.kdg.distrib.skeletonFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class SkeletonFactory {



    public static Skeleton createSkeleton(Object implementation) {

        InvocationHandler handler = new SkeletonInvocationHandler(implementation);
        Skeleton skeleton = (Skeleton) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{Skeleton.class}, handler);






        return skeleton;
    }
}
