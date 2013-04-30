package net.voaideahost.sslv.domain.ads;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.voaideahost.sslv.domain.addesc.NewAdDesc;


@Entity
@Table(name = "ads")
public class NewAd {

	@Id
	@GeneratedValue
	private long id;
	private String name;
	private Date created;
	private String owner;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
	private Set<NewAdDesc> items = new HashSet<NewAdDesc>(0);

	public Set<NewAdDesc> getList() {
		return items;
	}

	public long getId() {
		return id;
	}

	public void setName(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(java.util.Date cdate) {
		this.created = cdate;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setId(Long adid) {
		this.id = adid;
	}

}
