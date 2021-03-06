package domain.newaddesc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.FetchType;

import domain.newad.NewAd;

@Entity
@Table (name = "ad_desc")
public class NewAdDesc {

		@Id
		@GeneratedValue
		private long	id;
		@Column( name = "CRITERIA")
		private String 	criteria1;
		@Column( name = "VALUE")
		private String	value1;
		
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "ADS_ID", nullable = false)
		private NewAd newad;
		
		public NewAd getNewAd() {
		return this.newad;
		}
 
		public void setNewAd(NewAd newad) {
		this.newad = newad;
		}

		public String getCriteria1() {
			return criteria1;
		}
		public void setCriteria1(String criteria1) {
			this.criteria1 = criteria1;
		}
		public String getValue1() {
			return value1;
		}
		public void setValue1(String value1) {
			this.value1 = value1;
		}
		
}
