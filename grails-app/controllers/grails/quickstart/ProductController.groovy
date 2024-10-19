package grails.quickstart

class ProductController {

    def index() {
        render "Product list here"
    }

    def create() {
        def product = new Product(name: "Test product", price: 10.5, createdAt: new Date())
        product.save()
        System.out.print(product)
        flash.message = "Product saved: ${product.id}"
        redirect(action:index())
    }

    def show() {
        def product = Product.get(params.id)
        System.out.print(product)
        render(view: "display", product: product)
    }

}
