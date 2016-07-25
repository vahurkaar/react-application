var UserDataForm = React.createClass({
    getInitialState: function() {
        return {
            userDataForm: {},
            errors: {}
        };
    },
    handleChange: function(event) {
        this.state.userDataForm[event.target.id] = getChangeValue(event);
        this.setState({userDataForm: this.state.userDataForm});
    },
    componentDidMount: function() {
        $.ajax({
            url: this.props.dataUrl,
            type: 'GET',
            dataType: 'json',
            success: function (userData) {
                var userDataForm = {};

                userDataForm.userFullName = userData.name;
                userDataForm.modified = new Date(userData.modified);
                
                var userSectors = [];
                $.each(userData.userSectors, function(key, sector) {
                    userSectors.push(sector.id);
                });
                userDataForm.userSectors = userSectors;
                
                this.setState({userDataForm: userDataForm});
            }.bind(this)
        });
    },
    handleSubmit: function(event) {
        $.ajax({
            url: this.props.saveUrl,
            data: JSON.stringify(this.state.userDataForm),
            type: 'POST',
            contentType: 'application/json; charset=utf-8',
            success: function (userData) {
                this.setState({errors : []});
                this.componentDidMount();
            }.bind(this),
            error: function (response) {
                var errors = {};
                $.each(response.responseJSON, function(key, error) {
                    errors[error.field] = error.errorMessage;
                });
                this.setState({errors: errors});
            }.bind(this)
        });
        event.preventDefault();
    },
    render: function() {
        return (
            <form id="userDataForm" onSubmit={this.handleSubmit}>
                {this.state.userDataForm.modified && <p>Last updated: {this.state.userDataForm.modified.toString()}</p>}
                <p>Please enter your name and pick the Sectors you are currently involved in.</p>
                <div className="row">
                    <div className="form-group col-md-4">
                        <span className={this.state.errors && this.state.errors.userFullName ? 'has-error' : ''}>
                            <label htmlFor="userFullName" className="control-label required">Name: </label>
                            <input id="userFullName" className="form-control" type="text"
                                   value={this.state.userDataForm.userFullName} onChange={this.handleChange} />
                            <span className="help-block">{this.state.errors.userFullName}</span>
                        </span>
                    </div>
                </div>
                <div className="row">
                    <div className="form-group col-md-6">
                        <span className={this.state.errors.userSectors ? 'has-error' : ''}>
                            <label htmlFor="userSectors" className="control-label required">Sectors: </label>
                            <UserSectors url="/sectors/getAll" onChange={this.handleChange}
                                         selected={this.state.userDataForm.userSectors} />
                            <p className="help-block">{this.state.errors.userSectors}</p>
                        </span>
                    </div>
                </div>
            <span className="checkbox">
                <span key={this.state.userDataForm.modified} className={this.state.errors.agreeTerms ? 'has-error' : ''}>
                    <label htmlFor="agreeTerms" className="control-label required">
                        <input id="agreeTerms" type="checkbox" onChange={this.handleChange} />Agree to terms
                    </label>
                    <p className="help-block">{this.state.errors.agreeTerms}</p>
                </span>
            </span>
                <input id="saveUserData" className="btn btn-primary" type="submit" value="Save" />
            </form>
        )
    }
});

var UserSectors = React.createClass({
    getInitialState: function() {
        return {
            sectors: []
        };
    },
    componentDidMount: function() {
        $.ajax({
            url: this.props.url,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                this.setState({sectors: getSectors(data, 0)})
            }.bind(this)
        });
    },
    render: function() {
       var sectorOptions = this.state.sectors.map(function(sector) {
           return (
               <option key={sector.id} className={sector.styleClass} value={sector.id}>{sector.name}</option>
           );
       });

       return (
           <select id="userSectors" value={this.props.selected} className="form-control" multiple size="5" onChange={this.props.onChange}>
               {sectorOptions}
           </select>
       )
   }
});

ReactDOM.render(
<UserDataForm saveUrl="/userData/save" dataUrl="/userData/get" />,
    document.getElementById('userDataFormContainer')
);