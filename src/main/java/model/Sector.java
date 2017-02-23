package model;

/**
 * Created by inafalcao on 2/19/17.
 */
public class Sector {

    public String name;
    public int qtTickets = 0;
    public float basePrice = 0;
    public Long id; // id do setor
    public boolean enabled = true;

    /* case class Sector(name: String,
    nameAbrv: Option[String],
    capacity: Option[Int],
    blueprintId: Option[Long],
    localId: Long,
    id: Option[Long]) */

    public Sector(String name) {
        this.name = name;
    }

    //offers: List[TicketOffer] = List[TicketOffer]())

/*    ticketTypes
:
    Array[0]*/

}
