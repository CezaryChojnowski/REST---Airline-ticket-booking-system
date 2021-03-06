package pl.edu.pb.mongodbapplication.service

import org.junit.experimental.categories.Category
import pl.edu.pb.mongodbapplication.DTO.TicketDTOForTicketsListByUser
import pl.edu.pb.mongodbapplication.config.error.exception.FlightNotFoundException
import pl.edu.pb.mongodbapplication.config.error.exception.ReservationNotFoundException
import pl.edu.pb.mongodbapplication.config.error.exception.ThereAreTicketsForTheGivenFlightException
import pl.edu.pb.mongodbapplication.config.error.exception.TicketNotFoundException
import pl.edu.pb.mongodbapplication.config.error.exception.UserNotFoundException
import pl.edu.pb.mongodbapplication.model.AirPort
import pl.edu.pb.mongodbapplication.model.ERole
import pl.edu.pb.mongodbapplication.model.Flight
import pl.edu.pb.mongodbapplication.model.Roles
import pl.edu.pb.mongodbapplication.model.Ticket
import pl.edu.pb.mongodbapplication.model.User
import pl.edu.pb.mongodbapplication.repository.FlightRepository
import pl.edu.pb.mongodbapplication.repository.TicketRepository
import pl.edu.pb.mongodbapplication.repository.UserRepository
import org.springframework.core.env.Environment
import pl.edu.pb.mongodbapplication.typesOfTests.UnitTest;
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalTime

@Category(UnitTest.class)
class TicketServiceSpec extends Specification {
    TicketRepository ticketRepository
    UserRepository userRepository
    FlightRepository flightRepository
    EmailService emailService
    Environment env
    TicketService ticketService

    def setup() {
        ticketRepository = Stub(TicketRepository)
        userRepository = Stub(UserRepository)
        flightRepository = Stub(FlightRepository)
        emailService = Stub(EmailService)
        env = Stub(Environment)
        ticketService = new TicketService(
                ticketRepository,
                userRepository,
                flightRepository,
                emailService,
                env
        )
    }

    def userName = "testUserName"
    def ticketCode = 20
    def flightId = "1"
    def ticketId = "1"
    def roles = new HashSet(Arrays.asList(new Roles("1", ERole.ROLE_USER)));
    def user = new User("testFirstName", "testLastName", "testUserName", "testFirstName.testLastName@something.com", "testPassword", roles)
    def airPortFrom = new AirPort("test Country", "test City", "test AirPort")
    def airPortTo = new AirPort("test Country", "test City", "test AirPort")
    def date = new LocalDate(2021, 1, 20)
    def time = new LocalTime(20, 0, 0, 0);
    def price = 100

    def "Should return ticket by code"() {
        given: "Defined data to call method"
        def flight = new Flight(date, time, airPortFrom, airPortTo, price)
        def ticket = new Ticket(flight, user, ticketCode)
        def optionalTicket = Optional.of(ticket);
        and: "The findTicketByCode method will always return ticket when called with given ticket code"
        ticketRepository.findTicketByCode(ticketCode) >> optionalTicket
        when: "Try get ticket by number of reservation"
        Ticket ticket1 = ticketService.checkReservation(ticketCode);
        then:
        ticket1.equals(ticket)
    }

    def "Should thrown ReservationNotFoundException"() {
        given: "Defined data to call method"
        Optional<Ticket> optionalTicket = Optional.empty()
        and: "The findTicketByCode method will always return ticket when called with given ticket code"
        ticketRepository.findTicketByCode(ticketCode) >> optionalTicket
        when: "Try get ticket by number of reservation"
        ticketService.checkReservation(ticketCode);
        then:"Thrown ReservationNotFoundException"
        thrown(ReservationNotFoundException)
    }

    def "Should throw UserNoFoundException"() {
        given: "Defined data to call method"
        def userName = "username"
        when:"Try find all tickets by user"
        ticketService.findAllTicketsByUser(userName)
        then:"Thrown UserNotFoundException"
        thrown(UserNotFoundException)
    }

    def "Should return list of tickets"() {
        given: "Defined data to call method"
        def flight = new Flight(date, time, airPortFrom, airPortTo, price)
        Ticket ticket = new Ticket(flight, user, ticketCode)
        List<Ticket> tickets = Arrays.asList(ticket)
        Optional<User> optionalUser = Optional.of(user)
        List<TicketDTOForTicketsListByUser> ticketsDTO = Arrays.asList(new TicketDTOForTicketsListByUser(ticket.get_id(), ticket.getFlight(), ticket.getCode()))
        and: "The findByUsername method will always return optional user when called with given username" +
                "The findAllByUser method will always return tickets when called with given user" +
                "The getTicketsDTOByUser method will always return ticketsDTO called with given tickets list"
        userRepository.findByUsername(userName) >> optionalUser
        ticketRepository.findAllByUser(user) >> tickets
        ticketService.getTicketsDTOByUser(tickets) >> ticketsDTO
        when:"Try call findAllTicketsByUser method with given username"
        List<TicketDTOForTicketsListByUser> result = ticketService.findAllTicketsByUser(userName)
        then:
        result.equals(ticketsDTO)
    }

    def "Should thrown TicketNotFoundException"() {
        given: "Defined data to call method"
        Optional<Ticket> ticket = Optional.empty()
        and: "The findById method will always return ticket when called with given ticket id"
        ticketRepository.findById(ticketId) >> ticket
        when:"Try call getTicketById method with given ticket id"
        ticketService.getTicketById(ticketId)
        then:"Thrown TicketNotFoundException"
        thrown(TicketNotFoundException)
    }

    def "Should return ticket by ticket id"() {
        given: "Defined data to call method"
        def flight = new Flight(date, time, airPortFrom, airPortTo, price)
        Ticket ticket = new Ticket(flight, user, ticketCode)
        Optional<Ticket> optionalTicket = Optional.of(ticket)
        and: "The findById method will always return ticket when called with given ticket id"
        ticketRepository.findById(ticketId) >> optionalTicket
        when:"Try call getTicketById method with given ticket id"
        Ticket result = ticketService.getTicketById(ticketId)
        then:
        result.equals(ticket)
    }

    def "Should thrown FlightNotFoundException"() {
        given: "Defined data to call method"
        Optional<Flight> optionalFlight = Optional.empty()
        and: "The findById method will always return flight when called with given flight id"
        flightRepository.findById(flightId) >> optionalFlight
        when:"Try call checkIfThereAreTicketsForTheGivenFlight method with given flight id"
        ticketService.checkIfThereAreTicketsForTheGivenFlight(flightId)
        then:"Thrown FlightNotFoundException"
        thrown(FlightNotFoundException)
    }

    def "Should thrown ThereAreTicketsForTheGivenFlightException"() {
        given: "Defined data to call method"
        def flight = new Flight(date, time, airPortFrom, airPortTo, price)
        Ticket ticket = new Ticket(flight, user, ticketCode)
        List<Ticket> tickets = Arrays.asList(ticket)
        Optional<Flight> optionalFlight = Optional.of(flight)
        and: "The findById method will always return optional flight when called with given flight id" +
                "The findAllByFlight method will always return tickets when called with given flight"
        flightRepository.findById(flightId) >> optionalFlight
        ticketRepository.findAllByFlight(flight) >> tickets
        when:"Try call checkIfThereAreTicketsForTheGivenFlight method with given flight id"
        ticketService.checkIfThereAreTicketsForTheGivenFlight(flightId)
        then:"Thrown ThereAreTicketsForTheGivenFlightException"
        thrown(ThereAreTicketsForTheGivenFlightException)
    }
}
