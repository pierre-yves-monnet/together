// -----------------------------------------------------------
//
// HomeEvents
//
// HomeEvents page.Display the list of events
//
// -----------------------------------------------------------

class EventsList extends React.Component {
	
	// in props, a function must be give to the call back. When we click on a line, we call
	// this.props.selectEvent( event.id)
	constructor(props) {
		super();
		this.state = {  }
		console.log("EventsList.constructor - expected selectEvent in the props");
		this.state = { filterEvents: "", events: []};
		// this is mandatory to have access to the variable in the method... thank you React!   
		this.refreshListEvents = this.refreshListEvents.bind(this);
		this.refreshListEvents();

	}


	// -------------------------------------------- render
	render() {
		console.log("Home.constructor");
		// no map read, return
		if (! this.state.events) {
			return (<div />);
		}
		
		const listEventsHtml = this.state.events.map((event) =>
			  <tr onClick={() =>this.props.selectEvent( event.id)}>
				<td>{event.name}</td>
				<td>{event.dateevent}</td>
			 </tr>
		);
		return ( 
			<div class="container-fluid"> 
				<div class="row">
					<table><tr><td><h1>List events</h1> </td>
					 	<td><button type="button" class="btn btn-danger">Create an Event</button></td></tr>
				</table>
				</div>
				<div class="row">
					<span class="glyphicon glyphicon-menu-hamburger"></span>
					<span class="glyphicon glyphicon-user"></span>
				</div>
				<div class="row">
					<table class="table table-striped">
					<tr>
						<th>Name</th>
						<th>Date</th>
					</tr>
					{listEventsHtml}
					</table>
				</div>
			</div>)
	}
	
	// -------------------------------------------- Call REST
	refreshListEvents() {
		console.log("EventsList: http[events?filterEvents="+this.state.filterEvents+"]");
		this.setState( {events: []});
		
		const requestOptions = {
	        method: 'GET',
	        headers: { 'Content-Type': 'application/json' }
	    };
    	fetch('events?filterEvents='+this.state.filterEvents, requestOptions)
			.then(response => response.json())
        	.then( httpPayload => this.setListEvents( httpPayload ));
	}
	
	setListEvents( httpPayload ) {
		console.log("EventsList.setListEvents: connectStatus = "+JSON.stringify(httpPayload));
 		this.setState( {events: httpPayload.events});
		
	} // end connectStatus
}


console.log("EventsList Render id=" + document.getElementById('reactEventsList'));

// the marker is maybe not in the page
if (document.getElementById('reactEventsList'))
	ReactDOM.render(<EventsList />, document.getElementById('reactEventsList'));
