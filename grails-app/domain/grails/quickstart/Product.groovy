package grails.quickstart

class Product {

    Integer id
    String name
    Float price
    Date createdAt

    static mapping = {
        id generator: 'identity'
    }

    static constraints = {
    }
}
