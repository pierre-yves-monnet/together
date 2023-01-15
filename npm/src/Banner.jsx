// -----------------------------------------------------------
//
// Banner
//
// Banner controler. Display the banner in top.
//  Get the user information. If nobody are connected, be back immediately ! 
//
// -----------------------------------------------------------
import React from 'react';

import { FormattedMessage } from "react-intl";

import ReactCustomFlagSelect from 'react-custom-flag-select';
import "react-custom-flag-select/lib/react-custom-flag-select.min.css";

import Menu 			    	from './Menu';

import FactoryService 			from './service/FactoryService';

class Banner extends React.Component {
	
	// in props, a function must be give to the call back. When we click on a line, we call
	// this.props.changeLanguage( newLanguage )

	constructor( props ) {
		super();
		this.state = { language: props.language}
		this.clickMenuBanner                  = this.clickMenuBanner.bind( this )

	}



	render() {
		var authService = FactoryService.getInstance().getAuthService();
		// console.log("banner.render isconnected="+authService.isConnected());
		// 			<!--  green: #067c04;
		const FLAG_SELECTOR_OPTION_LIST = [
  	{ id: "en", name: 'US', displayText: 'English', locale: 'en_US', flag: "img/flags/en.svg" },
  	{ id: "fr", name: 'FR', displayText: 'Francais', locale: 'fr_FR', flag: "img/flags/fr.svg" },
  	{ id: "pt", name: 'BR', displayText: 'Portuges', locale: 'pt_BR', flag: "img/flags/pt.svg" },
  	{ id: "de", name: 'DE', displayText: 'Deutch', locale: 'de_DE', flag: "img/flags/de.svg" },
  	{ id: "el", name: 'GR', displayText: 'Greek', locale: 'el_GR', flag: "img/flags/el.svg" },
  	{ id: "hi", name: 'IN', displayText: 'Indian', locale: 'hi_IN', flag: "img/flags/hi.svg" },
  	{ id: "it", name: 'IT', displayText: 'Italiano', locale: 'it_IT', flag: "img/flags/it.svg" },
  	{ id: "ko", name: 'KR', displayText: 'Korean', locale: "ko_KR", flag: "img/flags/ko.svg" },
  	{ id: "ja", name: 'JP', displayText: 'Japanese', locale: "jp_JP", flag: "img/flags/ja.svg" },
  	{ id: "ar", name: 'LB', displayText: 'Arabish (Lebanon)', locale: 'ar_LB', flag: "img/flags/ar.svg" },
  	{ id: "ar", name: 'MO', displayText: 'Arabish (Marocco)', locale: 'ar_MA', flag: "img/flags/mo.svg" },
  	{ id: "es", name: 'ES', displayText: 'Spanish', locale: 'es_ES', flag: "img/flags/es.svg" }


		];
		
		var allFlags = (
		<ReactCustomFlagSelect
		   tabIndex= {'1' } //Optional.[Object].Modify wrapper general attributes.
			id={"language"}
		   name={"language"} //Optional.[Object].Modify button general attributes.
		   value={this.state.language} //Optional.[String].Default: "".
		   disabled={false} //Optional.[Bool].Default: false.
		   showArrow={true} //Optional.[Bool].Default: true.
		   animate={true} //Optional.[Bool].Default: false.
		   optionList={FLAG_SELECTOR_OPTION_LIST} //Required.[Array of Object(s)].Default: [].
		   // selectOptionListItemHtml={<div>us</div>} //Optional.[Html].Default: none. The custom select options item html that will display in dropdown list. Use it if you think the default html is ugly.
		   //selectHtml={currentSelection} //Optional.[Html].Default: none. The custom html that will display when user choose. Use it if you think the default html is ugly.
		   classNameWrapper="" //Optional.[String].Default: "".
		   classNameContainer="" //Optional.[String].Default: "".
		   classNameOptionListContainer="" //Optional.[String].Default: "".
		   classNameOptionListItem="" //Optional.[String].Default: "".
		   classNameDropdownIconOptionListItem={''} //Optional.[String].Default: "".
		   customStyleWrapper={{height:'20px', border: 'none'}} //Optional.[Object].Default: {}.
		   customStyleContainer={{ border: 'none', fontSize: '12px', height:'20px' }} //Optional.[Object].Default: {}.
		   customStyleSelect={{ width: '100px', border: 'none',  }} //Optional.[Object].Default: {}.
		   customStyleOptionListItem={{height:'30px'}} //Optional.[Object].Default: {}.
		   customStyleOptionListContainer={{ maxHeight: '200px', overflow: 'auto', width: '120px', marginTop: '11px' }} //Optional.[Object].Default: {}.
		  onChange={language => {
			console.log("Banner: changeLangage : "+language);
			this.setState( { language: language});
			this.props.changeLanguage( language );
								    }}
		    />
		)

		let user = authService.getUser();

        return (
            <div class="container-fluid">
                <div class="row">
                    <table style={{width: "100%"}}>
                    <tr>
                    <td style={{width: "10px"}}>
                        <img src="img/decor/logo_Togh_Banner.svg" alt="Togh"/>
                    </td>
                    <td >
                        <Menu showMenu={this.showMenu} clickMenu={this.clickMenuBanner} authCallback={this.authCallback}/>
                    </td>
                    <td style={{width: "10px"}}>
                        {allFlags}
                    </td>
                    {authService.isConnected() && user &&
                        <td style={{"color":"#888787", textAlign: "right" , verticalAlign: "middle", width: "200px"}}>

                            { user.typePicture=== 'TOGH' &&
                                <img  src="img/togh.png" style={{width:20}} alt="Togh"/>
                            }
                            { user.typePicture=== 'CYPRIS' &&
                                <img  src="img/cypris.png" style={{width:20}} alt="Cypris"/>
                            }
                            { user.typePicture=== 'URL' &&
                                <img  src={user.picture} style={{width:20}} alt="Url"/>
                            }
                            <br/>
                            {user.name}
                        </td>
                    }
                    </tr>
                    </table>
                </div>
            </div>)

	}


 	clickMenuBanner( menuName ) {
 		console.log("Banner.clickMenuBanner: menuAction=["+menuName+"]");
        this.props.clickMenu( menuName);
 	}

}	
export default Banner;


/**
if (authService.isConnected()) {
			// console.log("Banner: User Connected "+JSON.stringify(user));
			return (
				<div class="container-fluid">
					<div class="row">
						<div class="col-xs-12 banner">
						<table width="100%">
							<tr><td style={{"color":"#888787", verticalAlign: "top"}}>
								<img src="img/togh.png" style={{width:20}} alt="Togh" />
							</td><td style={{"color":"#888787", verticalAlign: "top", fontSize: "16px", fontFamily: "'Lato', 'Helvetica Neue', Helvetica, Arial, sans-serif"}}>
								Togh
							</td>
							<td style={{"color":"#888787", textAlign: "right" , verticalAlign: "top"}}>

								{allFlags}
								</td>

							</tr>
							</table>
						</div>
					</div>
				</div>
				)
		}
		else {
		**/