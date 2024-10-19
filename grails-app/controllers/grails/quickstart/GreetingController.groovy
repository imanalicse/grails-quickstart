package grails.quickstart

import grails.core.GrailsApplication

class GreetingController {
    GrailsApplication grailsApplication

    def index() {
        def port = grailsApplication.config.getProperty('server.port')
        render "Hello, Congratulations for your first Grails application! port: ${port}"
    }
}
