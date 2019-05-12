package ru.skillbench.tasks.javax.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;

public class XPathCallerImpl implements XPathCaller {
    private static Element[] getElements(Document src, String expression) {
        Element[] elements = null;
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression xPathExpression = xPath.compile(expression);
            NodeList nodeList = (NodeList) xPathExpression.evaluate(src, XPathConstants.NODESET);

            elements = new Element[nodeList.getLength()];
            for(int i = 0; i < nodeList.getLength(); i++) {
                elements[i] = (Element) nodeList.item(i);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return elements;
    }

    @Override
    public Element[] getEmployees(Document src, String deptno, String docType) {
        final String expression;
        if(docType.equals("emp-hier")) {
            expression = "//employee[@deptno = " + deptno + "]";
        } else {
            expression = "content/emp/employee[@deptno = " + deptno + "]";
        }
        return getElements(src, expression);
    }

    @Override
    public String getHighestPayed(Document src, String docType) {
        final String expression;
        if(docType.equals("emp-hier")) {
            expression = "//employee[not(./sal < //employee/sal)]/ename";
        } else {
            expression = "content/emp/employee[not(./sal < ../employee/sal)]/ename";
        }
        return getElements(src, expression)[0].getTextContent();
    }

    @Override
    public String getHighestPayed(Document src, String deptno, String docType) {
        final String expression;
        if(docType.equals("emp-hier")) {
            expression = "//employee[@deptno = " + deptno + " " +
                    "and not(./sal < //employee[@deptno = " + deptno + "]/sal)]/ename";
        } else {
            expression = "content/emp/employee[@deptno = " + deptno + " " +
                    "and not(./sal < ../employee[@deptno = " + deptno + "]/sal)]/ename";
        }
        return getElements(src, expression)[0].getTextContent();
    }

    @Override
    public Element[] getTopManagement(Document src, String docType) {
        final String expression;
        if(docType.equals("emp-hier")) {
            expression = "/employee";
        } else {
            expression = "content/emp/employee[not(@mgr)]";
        }
        return getElements(src, expression);
    }

    @Override
    public Element[] getOrdinaryEmployees(Document src, String docType) {
        final String expression;
        if(docType.equals("emp-hier")) {
            expression = "//employee[not(employee)]";
        } else {
            expression = "content/emp/employee[not(@empno = ../employee/@mgr)]";
        }
        return getElements(src, expression);
    }

    @Override
    public Element[] getCoworkers(Document src, String empno, String docType) {
        final String expression;
        if(docType.equals("emp-hier")) {
            expression = "//employee[@empno = " + empno + "]/../*[@empno != " + empno + "]";
        } else {
            expression = "content/emp/employee[@mgr = ../employee[@empno = " + empno + "]/@mgr" +
                    " and @empno != " + empno + "]";
        }
        return getElements(src, expression);
    }
}