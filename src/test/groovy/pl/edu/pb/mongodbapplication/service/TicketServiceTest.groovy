package pl.edu.pb.mongodbapplication.service
import org.junit.experimental.categories.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import pl.edu.pb.mongodbapplication.config.error.exception.FlightNotFoundException
import pl.edu.pb.mongodbapplication.config.error.exception.ReservationNotFoundException
import pl.edu.pb.mongodbapplication.config.error.exception.ThereAreTicketsForTheGivenFlightException
import pl.edu.pb.mongodbapplication.config.error.exception.UserNotFoundException
import pl.edu.pb.mongodbapplication.model.Flight
import pl.edu.pb.mongodbapplication.model.Ticket
import pl.edu.pb.mongodbapplication.repository.FlightRepository
import pl.edu.pb.mongodbapplication.repository.TicketRepository
import pl.edu.pb.mongodbapplication.typesOfTests.IntegrationTest
import spock.lang.Specification

@Category(IntegrationTest.class)
@ContextConfiguration
@SpringBootTest
@TestPropertySource("/application-test.properties")
class TicketServiceTest extends Specification {

    @Autowired
    private TicketService ticketService

    @Autowired
    private TicketRepository ticketRepository

    @Autowired
    private FlightRepository flightRepository

    def "Should return true when ticket has correct number of reservation"(){
        given: "Defined data to call method"
        def numberOfReservation = 10727
        when: "Try get ticket by number of reservation"
        def ticket = ticketService.checkReservation(numberOfReservation)
        then: "True if ticket has correct number of reservation"
        ticket.getCode().equals(numberOfReservation)
    }


    @Transactional
    def "Should thrown ReservationNotFoundException when ticket with given code doesn't exist"(){
        given: "Defined data to call method"
        def numberOfReservation = 99999
        when: "True if ticket has correct number of reservation"
        ticketService.checkReservation(numberOfReservation)
        then: "Thrown ReservationNotFoundException"
        thrown(ReservationNotFoundException)
    }

    @Transactional
    def "Should return not empty list of tickets given user"(){
        given: "Defined data to call method"
        def username = "testowy"
        when: "Try get all tickets by username"
        def tickets = ticketService.findAllTicketsByUser(username)
        then:"True if given user has ticket"
        !tickets.empty
    }

    def "Should return empty list of tickets when given user has no tickets"(){
        given: "Defined data to call method"
        def username = "testowy2"
        when: "Try get all tickets by username"
        def tickets = ticketService.findAllTicketsByUser(username)
        then:"True if given user has not ticket"
        tickets.empty
    }

    @Transactional
    def "Should thrown UserNotFoundException when user doesn't exist during try find all tickets that user"(){
        given: "Defined data to call method"
        def username = "doesntExistUser"
        when: "Try get all tickets by username"
        ticketService.findAllTicketsByUser(username)
        then: "Thrown UserNotFoundException"
        thrown(UserNotFoundException)
    }

    @Transactional
    def "Should delete all tickets to given flight"(){
        given: "Defined data to call method"
        def flightId = "5ff5b22806996948775b906b"
        Flight flight = flightRepository.findById(flightId).get()
        List<Ticket> ticketsByFlight = ticketRepository.findAllByFlight(flight)
        when: "Try delete all tickets by flight"
        ticketService.deleteTicketsByFlight(flightId)
        ticketsByFlight = ticketRepository.findAllByFlight(flight)
        then: "Flight has not ticket"
        ticketsByFlight.empty
    }

    @Transactional
    def "Should thrown FlightNotFoundException when flight doesn't exist during try delete all tickets that flight"(){
        given: "Defined data to call method"
        def flightId = "1"
        when: "Try delete all tickets by flight"
        ticketService.deleteTicketsByFlight(flightId)
        then: "Thrown FlightNotFoundException"
        thrown(FlightNotFoundException)
    }

    @Transactional
    def "Should thrown FlightNoTFoundException when try check if given flight has tickets, when flight doesnt exist"(){
        given: "Defined data to call method"
        def flightId = "1"
        when:"Check if there are tickets for the given flight"
        ticketService.checkIfThereAreTicketsForTheGivenFlight(flightId)
        then: "Thrown FlightNotFoundException"
        thrown(FlightNotFoundException)
    }

    @Transactional
    def "Should thrown ThereAreTicketsForTheGivenFlightException when flight has not tickets"(){
        given: "Defined data to call method"
        def flightId = "5ff5b22806996948775b906b"
        when:"Check if there are tickets for the given flight"
        ticketService.checkIfThereAreTicketsForTheGivenFlight(flightId)
        then: "Thrown ThereAreTicketsForTheGivenFlightException"
        thrown(ThereAreTicketsForTheGivenFlightException)
    }


}
