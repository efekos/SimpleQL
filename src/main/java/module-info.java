module SimpleQL {
    requires java.sql;
    requires org.slf4j;
    exports dev.efekos.simple_ql;
    exports dev.efekos.simple_ql.exception;
    exports dev.efekos.simple_ql.annotation;
    exports dev.efekos.simple_ql.data;
    exports dev.efekos.simple_ql.implementor;
    exports dev.efekos.simple_ql.query;
    exports dev.efekos.simple_ql.thread;
}